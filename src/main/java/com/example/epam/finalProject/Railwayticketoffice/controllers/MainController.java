package com.example.epam.finalProject.Railwayticketoffice.controllers;

import com.example.epam.finalProject.Railwayticketoffice.data.*;
import com.example.epam.finalProject.Railwayticketoffice.models.*;
import com.example.epam.finalProject.Railwayticketoffice.services.StationService;
import com.example.epam.finalProject.Railwayticketoffice.services.StopService;
import com.example.epam.finalProject.Railwayticketoffice.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@RequestMapping("")
public class MainController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    UserService userService;
    UserRepository userRepository;
    StationsRepository stationsRepository;
    StationService stationService;
    StopService stopService;
    StopRepository stopRepository;
    TicketRepository ticketRepository;
    MessageRepository messageRepository;

    public MainController(UserService userService, UserRepository userRepository,
                          StationsRepository stationsRepository, StationService stationService,
                          StopService stopService, StopRepository stopRepository,
                          TicketRepository ticketRepository,MessageRepository messageRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.stationsRepository = stationsRepository;
        this.stationService = stationService;
        this.stopService = stopService;
        this.stopRepository = stopRepository;
        this.ticketRepository=ticketRepository;
        this.messageRepository=messageRepository;
    }

    @GetMapping("/")
    public String startMethod(Model model){
        return "/en/index.html";
    }

    @GetMapping("/routes")
    public String routes(HttpServletRequest req , Model model){
        String from =req.getParameter("from");
        String to =req.getParameter("to");
        ArrayList<Route> routes = stopService.search(from, to);
        if (routes.isEmpty()) {
            model.addAttribute("search","search");
            return "/en/index.html";
        }
        model.addAttribute("routes",routes);
        return "/en/search.html";
    }

    @GetMapping("/route/{id}/details/{secondId}")
    public String getRouteDetails(@PathVariable long id , @PathVariable long secondId,
                                  Authentication authentication ,Model model){
        if (authentication==null) model.addAttribute("exist","exist");
        Optional<Stop> byId = stopRepository.findById(id);
        Stop stop = byId.get();
        List<Route> stops = stopService.check(id, secondId);
        Collections.sort(stops, new Comparator<Route>() {
            @Override
            public int compare(Route o1, Route o2) {
                return o1.getKm()-o2.getKm();
            }
        });
        model.addAttribute("id",id);
        model.addAttribute("secondId",secondId);
        model.addAttribute("stops",stops);
        return "/en/searchResult.html";
    }

    @GetMapping("/buy/{id}/{secondId}")
    @PreAuthorize("hasAuthority('USER')")
    public String buyRoute(@PathVariable long id , @PathVariable long secondId,
                           Authentication authentication , Model model){
        MyUser myUser = (MyUser) authentication.getPrincipal();
        Optional<Stop> byId1 = stopRepository.findById(id);
        Stop stop = byId1.get();
        List<Stop> allOfStops = stopRepository.findByTrain(stop.getTrain());
        AtomicInteger seat = new AtomicInteger();
        if (allOfStops.get(1).getSeats()>0){
            allOfStops.forEach(el->{
                seat.set(el.getSeats());
                int b = el.getSeats() -1;
                System.err.println(b);
                el.setSeats(b);
            });
            stopRepository.saveAll(allOfStops);
            int seatResult = seat.get();
            List<Route> stops = stopService.check(id, secondId);
            String first = "";
            String second = "";
            String date = "";
            for (Route el:stops){
                if (el.getFirstId()==id) {
                    date=el.getDeparture().replace('T',' ');
                    first = el.getStationFirst();
                }
                if (el.getSecondId()==secondId) second = el.getStationFirst();
            }
            String number = stops.get(1).getNumber();
            String uuid = UUID.randomUUID().toString();
            Ticket ticket = new Ticket(number,seatResult,uuid, myUser.getFirstName() + " " + myUser.getLastName(),
                    myUser.getDocumentNumber(),first,second,date);
            ticketRepository.save(ticket);
            model.addAttribute("date",date);
            model.addAttribute("seat",seatResult);
            model.addAttribute("first",first);
            model.addAttribute("second",second);
            model.addAttribute("number",number);
            model.addAttribute("uuid",uuid);
            return "/en/success.html";
        } else {
            model.addAttribute("exist","exist");
            return "/en/index.html";
        }
    }

    @GetMapping("/help")
    public String startHelp(){
        return "/en/help.html";
    }

    @PostMapping("/help/send")
    public String sendHelp(@RequestParam String name, @RequestParam String contact,
                           @RequestParam String text,Model model){
        messageRepository.save(new Message(name,contact,text));
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "/en/login.html";
    }

    @GetMapping("/logout-success")
    public String getLogoutPage(){
        return "/en/logout.html";
    }

    @GetMapping("/registration")
    public String getRegistration(Model model){
        model.addAttribute("user", new User());
        return "/en/registration.html";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute ("user") @Valid User user,
                          BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) return "/en/registration.html";
        if (!userService.addNewUser(user)) {
            model.addAttribute("exist", "exist");
        }
        return "/en/login.html";
    }
}