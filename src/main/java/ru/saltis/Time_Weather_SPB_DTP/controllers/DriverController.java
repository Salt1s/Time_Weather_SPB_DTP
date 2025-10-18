package ru.saltis.Time_Weather_SPB_DTP.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.saltis.Time_Weather_SPB_DTP.repositories.DriverRepository;

@Controller
@RequestMapping("/drivers")
public class DriverController {

    @Autowired
    private DriverRepository driverRepository;

    @GetMapping
    public String listDrivers(Model model) {
        try {
            model.addAttribute("drivers", driverRepository.findAll());
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке водителей: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при загрузке данных: " + e.getMessage());
        }
        return "drivers/list";
    }
}
