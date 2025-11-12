package ru.saltis.Time_Weather_SPB_DTP.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.saltis.Time_Weather_SPB_DTP.services.AnalyticsService;

@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/accidents")
    public String accidentsAnalytics(Model model) {
        model.addAttribute("hasAccidents", analyticsService.hasAccidentData());
        model.addAttribute("timeOfDayStats", analyticsService.getAccidentsByTimeOfDay());
        model.addAttribute("weatherStats", analyticsService.getAccidentsByWeather());
        model.addAttribute("roadStats", analyticsService.getAccidentsByRoadCondition());
        model.addAttribute("fatalStats", analyticsService.getFatalAccidentStats());
        return "analytics/accidents";
    }

    @GetMapping("/drivers")
    public String driversAnalytics(Model model) {
        model.addAttribute("hasDrivers", analyticsService.hasDriverData());
        model.addAttribute("genderStats", analyticsService.getDriverGenderStats());
        model.addAttribute("experienceStats", analyticsService.getDriverExperienceStats());
        return "analytics/drivers";
    }
}

