package ru.saltis.Time_Weather_SPB_DTP.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.saltis.Time_Weather_SPB_DTP.repositories.AccidentRepository;

@Controller
@RequestMapping("/accidents")
public class AccidentController {

    @Autowired
    private AccidentRepository accidentRepository;

    @GetMapping
    public String listAccidents(Model model) {
        try {
            System.out.println("Начинаем загрузку аварий...");
            var accidents = accidentRepository.findAll();
            System.out.println("Загружено аварий: " + accidents.size());
            model.addAttribute("accidents", accidents);
            System.out.println("Аварии добавлены в модель");
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке аварий: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при загрузке данных: " + e.getMessage());
        }
        return "accidents/list";
    }
}
