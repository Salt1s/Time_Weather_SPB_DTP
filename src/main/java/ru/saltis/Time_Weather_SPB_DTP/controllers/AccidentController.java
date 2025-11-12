package ru.saltis.Time_Weather_SPB_DTP.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.saltis.Time_Weather_SPB_DTP.models.Accident;
import ru.saltis.Time_Weather_SPB_DTP.repositories.AccidentRepository;
import ru.saltis.Time_Weather_SPB_DTP.repositories.DriverRepository;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/accidents")
public class AccidentController {

    @Autowired
    private AccidentRepository accidentRepository;

    @Autowired
    private DriverRepository driverRepository;

    @GetMapping
    public String listAccidents(Model model) {
        try {
            System.out.println("Начинаем загрузку аварий...");
            List<Accident> accidents = accidentRepository.findAll();
            System.out.println("Загружено аварий: " + accidents.size());

            Map<Long, Long> driverCounts = buildDriverCounts(accidents);

            long fatalAccidents = accidents.stream()
                    .filter(accident -> accident.getDeadCount() != null && accident.getDeadCount() > 0)
                    .count();

            long nonFatalAccidents = accidents.size() - fatalAccidents;

            int totalVictims = accidents.stream()
                    .mapToInt(accident -> accident.getDeadCount() != null ? accident.getDeadCount() : 0)
                    .sum();

            long totalDrivers = driverCounts.values().stream().mapToLong(Long::longValue).sum();
            long accidentsWithDrivers = driverCounts.values().stream().filter(count -> count > 0).count();

            model.addAttribute("totalAccidents", accidents.size());
            model.addAttribute("fatalAccidents", fatalAccidents);
            model.addAttribute("nonFatalAccidents", nonFatalAccidents);
            model.addAttribute("accidentsWithDrivers", accidentsWithDrivers);
            model.addAttribute("totalVictims", totalVictims);
            model.addAttribute("totalDrivers", totalDrivers);
            model.addAttribute("driverCounts", driverCounts);
            model.addAttribute("hasAccidents", !accidents.isEmpty());
            model.addAttribute("accidents", accidents);
            System.out.println("Аварии добавлены в модель");
        } catch (Exception e) {
            System.err.println("Ошибка при загрузке аварий: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Ошибка при загрузке данных: " + e.getMessage());
        }
        return "accidents/list";
    }

    private Map<Long, Long> buildDriverCounts(List<Accident> accidents) {
        List<Long> accidentIds = accidents.stream()
                .map(Accident::getId)
                .filter(Objects::nonNull)
                .toList();

        if (accidentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return driverRepository.countDriversByAccidentIds(accidentIds).stream()
                .collect(Collectors.toMap(
                        DriverRepository.AccidentDriverCount::getAccidentId,
                        DriverRepository.AccidentDriverCount::getDriverCount
                ));
    }
}
