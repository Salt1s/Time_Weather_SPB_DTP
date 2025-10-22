package ru.saltis.Time_Weather_SPB_DTP.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
    @GetMapping("/")
    public String helloPage(@RequestParam(value = "name", required = false) String name,
                            @RequestParam(value = "surname", required = false) String surname,
                            Model model) {

        // Безопасная обработка null значений
        String displayName = (name != null) ? name : "Guest";
        String displaySurname = (surname != null) ? surname : "";

        model.addAttribute("message", "Hello, " + displayName + " " + displaySurname);
        model.addAttribute("name", displayName);
        model.addAttribute("surname", displaySurname);

        return "/hello";
    }
}
