package ru.saltis.Time_Weather_SPB_DTP.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.saltis.Time_Weather_SPB_DTP.dto.GeoJsonFeature;
import ru.saltis.Time_Weather_SPB_DTP.dto.GeoJsonProperties;
import ru.saltis.Time_Weather_SPB_DTP.dto.GeoJsonParticipant;
import ru.saltis.Time_Weather_SPB_DTP.dto.GeoJsonVehicle;
import ru.saltis.Time_Weather_SPB_DTP.models.Accident;
import ru.saltis.Time_Weather_SPB_DTP.models.Driver;
import ru.saltis.Time_Weather_SPB_DTP.repositories.AccidentRepository;
import ru.saltis.Time_Weather_SPB_DTP.repositories.DriverRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class GeoJsonParserService {
    
    @Autowired
    private AccidentRepository accidentRepository;
    
    @Autowired
    private DriverRepository driverRepository;
    
    private final ObjectMapper objectMapper;
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    public GeoJsonParserService() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public boolean hasDataInDatabase() {
        long accidentCount = accidentRepository.count();
        long driverCount = driverRepository.count();
        return accidentCount > 0 || driverCount > 0;
    }

    public int parseAndSaveGeoJson() throws IOException {
        if (hasDataInDatabase()) {
            throw new IllegalStateException("База данных уже содержит данные. Очистите её перед повторным импортом.");
        }
        ClassPathResource resource = new ClassPathResource("sankt-peterburg.geojson");
        return parseAndSaveGeoJson(resource.getInputStream());
    }
    
    public int parseAndSaveGeoJson(InputStream inputStream) throws IOException {
        if (hasDataInDatabase()) {
            throw new IllegalStateException("База данных уже содержит данные. Очистите её перед повторным импортом.");
        }
        // Читаем GeoJSON как Map для получения features
        Map<String, Object> geoJson = objectMapper.readValue(inputStream, Map.class);
        List<Map<String, Object>> features = (List<Map<String, Object>>) geoJson.get("features");
        
        if (features == null) {
            throw new IOException("GeoJSON файл не содержит features");
        }
        
        int importedCount = 0;
        for (Map<String, Object> featureMap : features) {
            try {
                // Конвертируем Map в GeoJsonFeature
                GeoJsonFeature feature = objectMapper.convertValue(featureMap, GeoJsonFeature.class);
                
                // Создаем аварию из GeoJSON данных
                Accident accident = createAccidentFromGeoJson(feature);
                if (accident != null) {
                    accidentRepository.save(accident);
                    importedCount++;
                }
            } catch (Exception e) {
                System.err.println("Ошибка при обработке записи: " + e.getMessage());
            }
        }
        
        return importedCount;
    }
    
    @Transactional
    public void clearDatabase() {
        driverRepository.truncateTable();
        accidentRepository.truncateTable();
    }

    private Accident createAccidentFromGeoJson(GeoJsonFeature feature) {
        try {
            GeoJsonProperties properties = feature.getProperties();
            if (properties == null) {
                return null;
            }
            
            // Парсим дату и время
            LocalDateTime datetime = parseDateTime(properties.getDatetime());
            if (datetime == null) {
                return null;
            }
            
            // Создаем аварию
            Accident accident = new Accident();
            accident.setDatetime(datetime);
            accident.setDeadCount(properties.getDeadCount() != null ? properties.getDeadCount() : 0);
            
            // Устанавливаем коды для погоды и дороги (упрощенная логика)
            accident.setWeather(getWeatherCode(properties.getWeather()));
            accident.setRoad(getRoadCode(properties.getRoadConditions()));

            accident = accidentRepository.save(accident);

            createDriverRecords(accident, properties);
            
            return accident;
            
        } catch (Exception e) {
            System.err.println("Ошибка при создании аварии: " + e.getMessage());
            return null;
        }
    }

    private LocalDateTime parseDateTime(String dateTimeStr) {
        if (dateTimeStr == null || dateTimeStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            return LocalDateTime.parse(dateTimeStr, dateTimeFormatter);
        } catch (Exception e) {
            System.err.println("Ошибка парсинга даты: " + dateTimeStr + " - " + e.getMessage());
            return null;
        }
    }

    private Integer getWeatherCode(List<String> weatherList) {
        if (weatherList == null || weatherList.isEmpty()) {
            return 0; // Неизвестно
        }
        
        String weather = weatherList.get(0).toLowerCase();
        if (weather.contains("ясно")) return 1;
        if (weather.contains("облачно")) return 2;
        if (weather.contains("дождь")) return 3;
        if (weather.contains("снег")) return 4;
        if (weather.contains("туман")) return 5;
        return 0; // Другое
    }

    private Integer getRoadCode(List<String> roadConditions) {
        if (roadConditions == null || roadConditions.isEmpty()) {
            return 0; // Неизвестно
        }
        
        String road = roadConditions.get(0).toLowerCase();
        if (road.contains("сух")) return 1;
        if (road.contains("мокр")) return 2;
        if (road.contains("скользк")) return 3;
        if (road.contains("снежн")) return 4;
        return 0; // Другое
    }

    private void createDriverRecords(Accident accident, GeoJsonProperties properties) {
        // Ищем водителей в участниках
        if (properties.getParticipants() != null) {
            for (GeoJsonParticipant participant : properties.getParticipants()) {
                if ("Водитель".equals(participant.getRole()) && participant.getGender() != null) {
                    Driver driver = new Driver();
                    driver.setGender(participant.getGender());
                    driver.setYearsExp(participant.getYearsOfDrivingExperience() != null ? 
                        participant.getYearsOfDrivingExperience().toString() : "0");
                    driver.setAccident(accident);
                    driverRepository.save(driver);
                }
            }
        }

        if (properties.getVehicles() != null) {
            for (GeoJsonVehicle vehicle : properties.getVehicles()) {
                if (vehicle.getParticipants() != null) {
                    for (GeoJsonParticipant participant : vehicle.getParticipants()) {
                        if ("Водитель".equals(participant.getRole()) && participant.getGender() != null) {
                            Driver driver = new Driver();
                            driver.setGender(participant.getGender());
                            driver.setYearsExp(participant.getYearsOfDrivingExperience() != null ? 
                                participant.getYearsOfDrivingExperience().toString() : "0");
                            driver.setAccident(accident);
                            driverRepository.save(driver);
                        }
                    }
                }
            }
        }
    }
}

