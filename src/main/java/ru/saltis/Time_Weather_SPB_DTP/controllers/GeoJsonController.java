package ru.saltis.Time_Weather_SPB_DTP.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.saltis.Time_Weather_SPB_DTP.services.GeoJsonParserService;

import java.io.IOException;

@Controller
@RequestMapping("/geojson")
public class GeoJsonController {
    
    @Autowired
    private GeoJsonParserService geoJsonParserService;
    
    // Страница парсинга GeoJSON
    @GetMapping
    public String parsePage(Model model) {
        return "geojson/parse";
    }
    
    // Парсинг GeoJSON файла
    @PostMapping("/parse")
    public String parseGeoJson(Model model) {
        // Проверяем, есть ли уже данные в БД
        if (geoJsonParserService.hasDataInDatabase()) {
            model.addAttribute("error", "Ошибка: файл уже был распарсен ранее. В базе данных уже есть информация об авариях.");
            return "geojson/result";
        }
        
        try {
            int importedCount = geoJsonParserService.parseAndSaveGeoJson();
            model.addAttribute("success", "Успешно импортировано " + importedCount + " записей из GeoJSON");
            return "geojson/result";
        } catch (IOException e) {
            model.addAttribute("error", "Ошибка при парсинге GeoJSON: " + e.getMessage());
            return "geojson/result";
        }
    }
}

