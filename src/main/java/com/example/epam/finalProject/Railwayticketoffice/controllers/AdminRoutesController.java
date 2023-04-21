package com.example.epam.finalProject.Railwayticketoffice.controllers;

import com.example.epam.finalProject.Railwayticketoffice.data.StationsRepository;
import com.example.epam.finalProject.Railwayticketoffice.data.StopRepository;
import com.example.epam.finalProject.Railwayticketoffice.data.UserRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Contact;
import com.example.epam.finalProject.Railwayticketoffice.models.Station;
import com.example.epam.finalProject.Railwayticketoffice.models.Stop;
import com.example.epam.finalProject.Railwayticketoffice.services.StationService;
import com.example.epam.finalProject.Railwayticketoffice.services.StopService;
import com.example.epam.finalProject.Railwayticketoffice.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminRoutesController {

    UserService userService;
    UserRepository userRepository;
    StationsRepository stationsRepository;
    StationService stationService;
    StopService stopService;
    StopRepository stopRepository;

    public AdminRoutesController(UserService userService, UserRepository userRepository,
                                 StationsRepository stationsRepository,
                                 StationService stationService, StopService stopService,
                                 StopRepository stopRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.stationsRepository = stationsRepository;
        this.stationService = stationService;
        this.stopService = stopService;
        this.stopRepository = stopRepository;
    }

    @GetMapping("/stations")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getStations(Model model){
        Iterable <Station> stationsAll = stationsRepository.findAll();
        List <Station> stations = new ArrayList<>();
        stationsAll.forEach(stations::add);
        Collections.sort(stations, new Comparator<Station>() {
            @Override
            public int compare(Station o1, Station o2) {
                return o1.getStreet().compareTo(o2.getStreet());
            }
        });
        model.addAttribute("stations", stations);
        model.addAttribute("station", new Station());
        return "/admin/stations.html";
    }

    @PostMapping("/addStation")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addContacts (@ModelAttribute ("station") @Valid Station station,
                               BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            Iterable <Station> stations = stationsRepository.findAll();
            model.addAttribute("stations", stations);
            return "/admin/stations.html";
        }
        stationService.addNewStation(station);
        return "redirect:/admin/stations";
    }

    @GetMapping("/station/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteContact (@PathVariable("id") long id, Model model) {
        stationsRepository.deleteById(id);
        return "redirect:/admin/stations";
    }


    @GetMapping("/routes")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String Routes(Model model){
        Iterable <Station> stationsAll = stationsRepository.findAll();
        List <Station> stations = new ArrayList<>();
        stationsAll.forEach(stations::add);
        Collections.sort(stations, new Comparator<Station>() {
            @Override
            public int compare(Station o1, Station o2) {
                return o1.getStreet().compareTo(o2.getStreet());
            }
        });
        model.addAttribute("stations", stations);
        return "/admin/routes.html";
    }

    @PostMapping("/newRoute")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createRoute(@RequestParam long departure, @RequestParam long arrival,
                              @RequestParam int km, @RequestParam String train,
                              @RequestParam LocalDateTime timein, @RequestParam LocalDateTime timeout, Model model){
        Stop first = new Stop(train,timeout,timeout,0);
        Stop second = new Stop(train,timein,timein,km);
        if (!stopService.createNewRoute(first,second,departure,arrival)) {
            model.addAttribute("exist", "exist");
            Iterable <Station> stations = stationsRepository.findAll();
            model.addAttribute("stations", stations);
            return "/admin/routes.html";
        }
        return "redirect:/admin/routes";
    }


    @PostMapping("/stop/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteStop (@RequestParam long station, @RequestParam String number,
                               Model model) {
        if (!stopService.delete(station,number)){
            model.addAttribute("notExist", "notExist");
            Iterable <Station> stations = stationsRepository.findAll();
            model.addAttribute("stations", stations);
            return "/admin/routes.html";
        }
        return "redirect:/admin/routes";
    }

    @PostMapping("/stop/changeStop")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String changeStop (@RequestParam long station, @RequestParam String number,
                              @RequestParam int km,
                              @RequestParam LocalDateTime timein, @RequestParam LocalDateTime timeout,
                               Model model) {
        if (!stopService.change(station,number,km,timein,timeout)){
            model.addAttribute("notChange", "notChange");
            Iterable <Station> stations = stationsRepository.findAll();
            model.addAttribute("stations", stations);
            return "/admin/routes.html";
        }
        return "redirect:/admin/routes";
    }

    @GetMapping("/routes/check")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String checkRoutes(Model model){
        Iterable <Stop> stop = stopRepository.findAll();
        List <Stop> stops = new ArrayList<>();
        stop.forEach(stops::add);
        Collections.sort(stops, new Comparator<Stop>() {
            @Override
            public int compare(Stop o1, Stop o2) {
                return o1.getStation().getCity().compareTo(o2.getStation().getCity());
            }
        });
        model.addAttribute("stops", stops);
        return "/admin/checkRoutes.html";
    }

    @PostMapping("/addStop")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addStop (@RequestParam long station, @RequestParam String number,
                           @RequestParam int kil,
                           @RequestParam LocalDateTime in, @RequestParam LocalDateTime out, Model model) {
        stopService.addStop(station,number,kil,in,out);
        return "redirect:/admin/routes/check";
    }
    
    

    @PostMapping("/deleteRoute")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteRoute (@RequestParam String number, Model model) {
        List <Stop> stops = stopRepository.findByTrain(number);
        if (!stops.isEmpty()){
            stopRepository.deleteAll(stopRepository.findByTrain(number));
        }
        return "redirect:/admin/routes/check";
    }

    @GetMapping("/searchRoute")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String searchRoute (HttpServletRequest req, Model model) {
        String number = req.getParameter("number");
        List <Stop> stops = stopRepository.findByTrain(number);
        if (stops.isEmpty()) return "redirect:/admin/routes/check";
        Collections.sort(stops, new Comparator<Stop>() {
            @Override
            public int compare(Stop o1, Stop o2) {
                return o1.getStation().getCity().compareTo(o2.getStation().getCity());
            }
        });
        model.addAttribute("stops",stops);
        return "/admin/searchRoute.html";
    }

}
