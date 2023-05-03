package com.example.epam.finalProject.Railwayticketoffice.controllers;

import com.example.epam.finalProject.Railwayticketoffice.data.StationsRepository;
import com.example.epam.finalProject.Railwayticketoffice.data.StopRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Station;
import com.example.epam.finalProject.Railwayticketoffice.models.Stop;
import com.example.epam.finalProject.Railwayticketoffice.services.StationService;
import com.example.epam.finalProject.Railwayticketoffice.services.StopService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * The controller 'AdminRoutesController' com.example.epam.finalProject.Railwayticketoffice.controllers.
 * It's responsible for viewing, editing and deleting stations and routes with stops.
 * @author Ivan Volchenko
 */
@Controller
public class AdminRoutesController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminRoutesController.class);
    private final StationsRepository stationsRepository;
    private final StationService stationService;
    private final StopService stopService;
    private final StopRepository stopRepository;

    public AdminRoutesController(StationsRepository stationsRepository,
                                 StationService stationService, StopService stopService,
                                 StopRepository stopRepository) {
        this.stationsRepository = stationsRepository;
        this.stationService = stationService;
        this.stopService = stopService;
        this.stopRepository = stopRepository;
    }

    @GetMapping("/admin/stations")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getStations(Model model){
        LOGGER.info("AdminRoutesController: method 'getStations'");
        Iterable <Station> stationsAll = stationsRepository.findAll();
        List <Station> stations = new ArrayList<>();
        stationsAll.forEach(stations::add);
        stations.sort(Comparator.comparing(Station::getStreet));
        model.addAttribute("stations", stations);
        model.addAttribute("station", new Station());
        return "/admin/stations.html";
    }

    @PostMapping("/admin/addStation")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addStation (@ModelAttribute ("station") @Valid Station station,
                               BindingResult bindingResult, Model model) {
        LOGGER.info("AdminRoutesController: method 'addContacts'");
        if (bindingResult.hasErrors()) {
            Iterable <Station> stations = stationsRepository.findAll();
            model.addAttribute("stations", stations);
            return "/admin/stations.html";
        }
        stationService.addNewStation(station);
        return "redirect:/admin/stations";
    }

    @GetMapping("/admin/station/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteContact (@PathVariable("id") long id, Model model) {
        Optional<Station> station = stationsRepository.findById(id);
        if (station.isEmpty()) return "redirect:/admin/stations";
        stationsRepository.deleteById(id);
        return "redirect:/admin/stations";
    }


    @GetMapping("/admin/routes")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getRoutes(Model model){
        LOGGER.info("AdminRoutesController: method 'getRoutes'");
        Iterable <Station> stationsAll = stationsRepository.findAll();
        List <Station> stations = new ArrayList<>();
        stationsAll.forEach(stations::add);
        stations.sort(Comparator.comparing(Station::getStreet));
        model.addAttribute("stations", stations);
        return "/admin/routes.html";
    }

    @PostMapping("/admin/newRoute")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createRoute(@RequestParam long departure, @RequestParam long arrival,
                              @RequestParam int km, @RequestParam String train,
                              @RequestParam LocalDateTime timein, @RequestParam LocalDateTime timeout, Model model){
        LOGGER.info("AdminRoutesController: method 'createRoute'");
        Stop firstStop = new Stop(train,timeout,timeout,0);
        Stop secondStop = new Stop(train,timein,timein,km);
        if (!stopService.createNewRoute(firstStop,secondStop,departure,arrival)) {
            return "redirect:/admin/routes/error";
        }
        return "redirect:/admin/routes";
    }

    @GetMapping("/admin/routes/error")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getRoutesError(Model model){
        LOGGER.info("AdminRoutesController: method 'getRoutesError'");
        Iterable <Station> stationsAll = stationsRepository.findAll();
        List <Station> stations = new ArrayList<>();
        stationsAll.forEach(stations::add);
        stations.sort(Comparator.comparing(Station::getStreet));
        model.addAttribute("stations", stations);
        model.addAttribute("exist", "exist");
        return "/admin/routes.html";
    }


    @PostMapping("/admin/stop/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteStop (@RequestParam long st, Model model) {
        LOGGER.info("AdminRoutesController: method 'deleteStop'");
        if (!stopService.deleteStop(st)){
            return "redirect:/routes/check/error";
        }
        return "redirect:/routes/check";
    }

    @PostMapping("/admin/stop/changeStop")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String changeStop (@RequestParam long station, @RequestParam int point,
                              @RequestParam LocalDateTime timein, @RequestParam LocalDateTime timeout,
                               Model model) {
        LOGGER.info("AdminRoutesController: method 'changeStop'");
        if (!stopService.change(station,point,timein,timeout)){
            return "redirect:/routes/check/error";
        }
        return "redirect:/routes/check";
    }

    @GetMapping("/routes/check/error")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getCheckRoutesError(Model model){
        LOGGER.info("AdminRoutesController: method 'getCheckRoutesError'");
        model.addAttribute("notExist", "notExist");
        model.addAttribute("notChange", "notChange");
        return checkRoute(1,model);
    }

    @GetMapping("/routes/check")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String checkRoutes(Model model){
        LOGGER.info("AdminRoutesController: method 'checkRoutes'");
        return checkRoute(1,model);
    }

    @GetMapping("/routes/check/{pageNu}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String checkRoute(@PathVariable (value = "pageNu") int pageNu,Model model){
        LOGGER.info("AdminRoutesController: method 'checkRoute'");
        int size = 12;
        Page<Stop> page = stopService.findAllStops(pageNu,size);
        List <Stop> stopsList = page.getContent();
        ArrayList<Stop> stops = new ArrayList<>(stopsList);
        stops.sort(Comparator.comparing(Stop::getTrain));
        model.addAttribute("zero",1);
        model.addAttribute("currentPage",pageNu);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("stops", stops);
        return "/admin/checkRoutes.html";
    }

    @PostMapping("/addStop")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addStop (@RequestParam long station, @RequestParam String number,
                           @RequestParam int kil,
                           @RequestParam LocalDateTime in, @RequestParam LocalDateTime out, Model model) {
        LOGGER.info("AdminRoutesController: method 'addStop'");
        if(!stopService.addStop(station,number,kil,in,out)){
            return "redirect:/routes/check/error";
        }
        return "redirect:/routes/check";
    }
    

    @PostMapping("/admin/deleteRoute")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteRoute (@RequestParam String code, Model model) {
        LOGGER.info("AdminRoutesController: method 'deleteRoute'");
        List <Stop> stops = stopRepository.findByTrain(code);
        if (!stops.isEmpty()){
            stopRepository.deleteAll(stopRepository.findByTrain(code));
        }
        return "redirect:/admin/routes";
    }

    @GetMapping("/admin/searchRoute")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String searchRoute (HttpServletRequest req, Model model) {
        LOGGER.info("AdminRoutesController: method 'searchRoute'");
        String number = req.getParameter("number");
        List <Stop> stops = stopRepository.findByTrain(number);
        if (stops.isEmpty()) return "redirect:/admin/routes";
        stops.sort(Comparator.comparingInt(Stop::getKm));
        model.addAttribute("stops",stops);
        return "/admin/searchRoute.html";
    }

}
