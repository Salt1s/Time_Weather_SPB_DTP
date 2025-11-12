package ru.saltis.Time_Weather_SPB_DTP.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.saltis.Time_Weather_SPB_DTP.services.GeoJsonParserService;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/geojson")
public class GeoJsonController {
    
    @Autowired
    private GeoJsonParserService geoJsonParserService;
    
    // Страница парсинга GeoJSON
    @GetMapping
    public String parsePage(Model model) {
        boolean hasData = geoJsonParserService.hasDataInDatabase();
        model.addAttribute("hasData", hasData);
        return "geojson/parse";
    }
    
    // Парсинг GeoJSON файла
    @PostMapping("/parse")
    public String parseGeoJson(@RequestParam(value = "file", required = false) MultipartFile file, Model model) {
        try {
            if (geoJsonParserService.hasDataInDatabase()) {
                model.addAttribute("error", "Импорт невозможен: база данных уже содержит записи. Очистите данные перед повторной загрузкой.");
                model.addAttribute("hasData", true);
                return "geojson/parse";
            }

            int importedCount;
            
            // Если файл загружен, используем его, иначе используем встроенный
            if (file != null && !file.isEmpty()) {
                // Валидация типа файла
                String fileName = file.getOriginalFilename();
                if (fileName == null || !fileName.toLowerCase().endsWith(".geojson")) {
                    model.addAttribute("error", "Ошибка: выбранный файл не является GeoJSON файлом. Пожалуйста, выберите файл с расширением .geojson");
                    model.addAttribute("hasData", geoJsonParserService.hasDataInDatabase());
                    return "geojson/parse";
                }
                
                // Парсим загруженный файл
                try (InputStream inputStream = file.getInputStream()) {
                    importedCount = geoJsonParserService.parseAndSaveGeoJson(inputStream);
                    model.addAttribute("success", "Успешно импортировано " + importedCount + " записей из загруженного файла: " + fileName);
                }
            } else {
                // Используем встроенный файл
                importedCount = geoJsonParserService.parseAndSaveGeoJson();
                model.addAttribute("success", "Успешно импортировано " + importedCount + " записей из встроенного файла sankt-peterburg.geojson");
            }
            
            return "geojson/result";
        } catch (IOException e) {
            model.addAttribute("error", "Ошибка при парсинге GeoJSON: " + e.getMessage());
            return "geojson/result";
        } catch (Exception e) {
            model.addAttribute("error", "Неожиданная ошибка: " + e.getMessage());
            return "geojson/result";
        }
    }
    
    // Очистка базы данных
    @PostMapping("/clear")
    public String clearDatabase(RedirectAttributes redirectAttributes) {
        try {
            geoJsonParserService.clearDatabase();
            redirectAttributes.addFlashAttribute("success", "База данных успешно очищена. Все записи об авариях и водителях удалены.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при очистке базы данных: " + e.getMessage());
        }
        return "redirect:/geojson";
    }
}

