package ru.saltis.Time_Weather_SPB_DTP.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.saltis.Time_Weather_SPB_DTP.models.Driver;
import ru.saltis.Time_Weather_SPB_DTP.repositories.DriverRepository;

import java.util.List;

@Controller
@RequestMapping("/drivers")
public class DriverController {

    @Autowired
    private DriverRepository driverRepository;

    @GetMapping
    public String listDrivers(Model model) {
        try {
            List<Driver> drivers = driverRepository.findAll();

            long maleDrivers = drivers.stream()
                    .filter(driver -> "Мужской".equalsIgnoreCase(driver.getGender()))
                    .count();

            long femaleDrivers = drivers.stream()
                    .filter(driver -> "Женский".equalsIgnoreCase(driver.getGender()))
                    .count();

            double averageExperience = drivers.stream()
                    .map(Driver::getYearsExp)
                    .mapToDouble(value -> {
                        if (value == null || value.isBlank()) {
                            return 0.0;
                        }
                        try {
                            return Double.parseDouble(value);
                        } catch (NumberFormatException e) {
                            return 0.0;
                        }
                    })
                    .average()
                    .orElse(0.0);

            model.addAttribute("drivers", drivers);
            model.addAttribute("totalDrivers", drivers.size());
            model.addAttribute("maleDrivers", maleDrivers);
            model.addAttribute("femaleDrivers", femaleDrivers);
            model.addAttribute("averageExperience", averageExperience);
            model.addAttribute("hasDrivers", !drivers.isEmpty());
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке водителей: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при загрузке данных: " + e.getMessage());
        }
        return "drivers/list";
    }
}
