package ru.saltis.Time_Weather_SPB_DTP.services;

import org.springframework.stereotype.Service;
import ru.saltis.Time_Weather_SPB_DTP.models.Accident;
import ru.saltis.Time_Weather_SPB_DTP.models.Driver;
import ru.saltis.Time_Weather_SPB_DTP.repositories.AccidentRepository;
import ru.saltis.Time_Weather_SPB_DTP.repositories.DriverRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private final AccidentRepository accidentRepository;
    private final DriverRepository driverRepository;

    public AnalyticsService(AccidentRepository accidentRepository, DriverRepository driverRepository) {
        this.accidentRepository = accidentRepository;
        this.driverRepository = driverRepository;
    }

    public Map<String, Long> getAccidentsByTimeOfDay() {
        Map<String, Long> buckets = new LinkedHashMap<>();
        buckets.put("Ночь (00:00-05:59)", 0L);
        buckets.put("Утро (06:00-11:59)", 0L);
        buckets.put("День (12:00-17:59)", 0L);
        buckets.put("Вечер (18:00-23:59)", 0L);

        List<Accident> accidents = accidentRepository.findAll();
        for (Accident accident : accidents) {
            String bucket = resolveTimeOfDay(accident.getDatetime());
            if (bucket != null) {
                buckets.computeIfPresent(bucket, (k, v) -> v + 1);
            }
        }

        return buckets;
    }

    public Map<String, Long> getAccidentsByWeather() {
        Map<String, Long> buckets = new LinkedHashMap<>();
        buckets.put("Ясно", 0L);
        buckets.put("Облачно", 0L);
        buckets.put("Дождь", 0L);
        buckets.put("Снег", 0L);
        buckets.put("Туман", 0L);

        List<Accident> accidents = accidentRepository.findAll();
        for (Accident accident : accidents) {
            String bucket = resolveWeather(accident.getWeather());
            if (bucket != null) {
                buckets.computeIfPresent(bucket, (k, v) -> v + 1);
            }
        }

        return buckets;
    }

    public Map<String, Long> getAccidentsByRoadCondition() {
        Map<String, Long> buckets = new LinkedHashMap<>();
        buckets.put("Не указано", 0L);
        buckets.put("Сухая", 0L);
        buckets.put("Мокрая", 0L);
        buckets.put("Скользкая", 0L);
        buckets.put("Снежная", 0L);

        List<Accident> accidents = accidentRepository.findAll();
        for (Accident accident : accidents) {
            String bucket = resolveRoadCondition(accident.getRoad());
            if (bucket != null) {
                buckets.computeIfPresent(bucket, (k, v) -> v + 1);
            }
        }

        return buckets;
    }

    public Map<String, Long> getFatalAccidentStats() {
        Map<String, Long> buckets = new LinkedHashMap<>();
        buckets.put("Со смертельным исходом", 0L);
        buckets.put("Без смертельного исхода", 0L);

        List<Accident> accidents = accidentRepository.findAll();
        for (Accident accident : accidents) {
            boolean fatal = accident.getDeadCount() != null && accident.getDeadCount() > 0;
            String bucket = fatal ? "Со смертельным исходом" : "Без смертельного исхода";
            buckets.computeIfPresent(bucket, (k, v) -> v + 1);
        }

        return buckets;
    }

    public Map<String, Long> getDriverGenderStats() {
        Map<String, Long> buckets = new LinkedHashMap<>();
        buckets.put("Мужчины", 0L);
        buckets.put("Женщины", 0L);

        List<Driver> drivers = driverRepository.findAll();
        for (Driver driver : drivers) {
            String bucket = resolveGender(driver.getGender());
            if (bucket != null) {
                buckets.computeIfPresent(bucket, (k, v) -> v + 1);
            }
        }

        return buckets;
    }

    public Map<String, Long> getDriverExperienceStats() {
        Map<String, Long> buckets = new LinkedHashMap<>();
        List<String> orderedBuckets = Arrays.asList(
                "До 1 года",
                "1-3 года",
                "3-5 лет",
                "5-10 лет",
                "10-15 лет",
                "15-20 лет",
                "20-25 лет",
                "25-30 лет",
                "30+ лет"
        );
        orderedBuckets.forEach(bucket -> buckets.put(bucket, 0L));

        List<Driver> drivers = driverRepository.findAll();
        for (Driver driver : drivers) {
            String bucket = resolveExperienceBucket(driver.getYearsExp());
            if (bucket != null) {
                buckets.computeIfPresent(bucket, (k, v) -> v + 1);
            }
        }

        return buckets;
    }

    public boolean hasAccidentData() {
        return accidentRepository.count() > 0;
    }

    public boolean hasDriverData() {
        return driverRepository.count() > 0;
    }

    private String resolveTimeOfDay(LocalDateTime datetime) {
        if (datetime == null) {
            return null;
        }
        int hour = datetime.getHour();
        if (hour < 6) {
            return "Ночь (00:00-05:59)";
        } else if (hour < 12) {
            return "Утро (06:00-11:59)";
        } else if (hour < 18) {
            return "День (12:00-17:59)";
        } else {
            return "Вечер (18:00-23:59)";
        }
    }

    private String resolveWeather(Integer code) {
        if (code == null) {
            return null;
        }
        return switch (code) {
            case 1 -> "Ясно";
            case 2 -> "Облачно";
            case 3 -> "Дождь";
            case 4 -> "Снег";
            case 5 -> "Туман";
            default -> null;
        };
    }

    private String resolveRoadCondition(Integer code) {
        if (code == null) {
            return "Не указано";
        }
        return switch (code) {
            case 0 -> "Не указано";
            case 1 -> "Сухая";
            case 2 -> "Мокрая";
            case 3 -> "Скользкая";
            case 4 -> "Снежная";
            default -> null;
        };
    }

    private String resolveGender(String gender) {
        if (gender == null) {
            return null;
        }
        String normalized = gender.trim().toLowerCase();
        if (normalized.contains("муж")) {
            return "Мужчины";
        }
        if (normalized.contains("жен")) {
            return "Женщины";
        }
        return null;
    }

    private String resolveExperienceBucket(String yearsExp) {
        Integer years = parseYearsOfExperience(yearsExp);
        if (years == null) {
            return null;
        }
        if (years < 1) {
            return "До 1 года";
        }
        if (years < 3) {
            return "1-3 года";
        }
        if (years < 5) {
            return "3-5 лет";
        }
        if (years < 10) {
            return "5-10 лет";
        }
        if (years < 15) {
            return "10-15 лет";
        }
        if (years < 20) {
            return "15-20 лет";
        }
        if (years < 25) {
            return "20-25 лет";
        }
        if (years < 30) {
            return "25-30 лет";
        }
        return "30+ лет";
    }

    private Integer parseYearsOfExperience(String value) {
        if (value == null) {
            return null;
        }
        String digitsOnly = value.replaceAll("[^0-9]", "");
        if (digitsOnly.isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(digitsOnly);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}

