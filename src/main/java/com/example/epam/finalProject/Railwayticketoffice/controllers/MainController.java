package com.example.epam.finalProject.Railwayticketoffice.controllers;

import com.example.epam.finalProject.Railwayticketoffice.data.*;
import com.example.epam.finalProject.Railwayticketoffice.models.*;
import com.example.epam.finalProject.Railwayticketoffice.services.StationService;
import com.example.epam.finalProject.Railwayticketoffice.services.StopService;
import com.example.epam.finalProject.Railwayticketoffice.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The main controller 'MainController' com.example.epam.finalProject.Railwayticketoffice.controllers.
 * It's responsible for the main page,searching and viewing routes,
 * their purchase, the 'help' department, login and registration.
 * @author Ivan Volchenko
 */
@Controller
@RequestMapping("")
public class MainController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);
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
        LOGGER.info("Home page");
        return "/en/index.html";
    }

    @GetMapping("/routes")
    public String searchRoutes(HttpServletRequest req , Model model){
        LOGGER.info("Main controller: method 'routes'");
        String from =req.getParameter("from");
        String to =req.getParameter("to");
        return searchPageRoutes(from,to,1,model);

    }

    @GetMapping("/routes/{from}/{to}/{pageNu}")
    public String searchPageRoutes(@PathVariable (value = "from") String from,@PathVariable (value = "to") String to,
                               @PathVariable (value = "pageNu") int pageNu, Model model){
        LOGGER.info("Main controller: method 'routes/{from}/{to}/{pageNu}'");
        int size = 3;
        ArrayList<Route> routes = stopService.search(from, to);
        if (routes.isEmpty()) {
            model.addAttribute("search","search");
            return "/en/index.html";
        }
        long totalItems = routes.size();
        double result = (double)totalItems/size;
        int totalPages = (int) Math.ceil(result);
        ArrayList <Route> routesFinal = new ArrayList<>();
        if (pageNu>totalPages) return "redirect:/routes";
        int a = size*(pageNu-1);
        int b;
        if (pageNu<totalPages) b = size*pageNu;
        else b = (int) (totalItems);
        for (int c=a;c<b;c++){
            routesFinal.add(routes.get(c));
        }
        model.addAttribute("from",from);
        model.addAttribute("to",to);
        model.addAttribute("zero",1);
        model.addAttribute("currentPage",pageNu);
        model.addAttribute("totalPages",totalPages);
        model.addAttribute("totalItems",totalItems);
        model.addAttribute("routes",routesFinal);
        return "/en/search.html";
    }

    @GetMapping("/route/{id}/details/{secondId}")
    public String getRouteDetails(@PathVariable long id , @PathVariable long secondId,
                                  Authentication authentication ,Model model){
        LOGGER.info("Main controller: method 'getRouteDetails'");
        if (authentication==null) model.addAttribute("exist","exist");
        List<Route> stops = stopService.check(id, secondId);
        stops.sort(Comparator.comparingInt(Route::getKm));
        String number = stops.get(0).getNumber();
        String time = stopService.calculateTime(stopRepository.findById(id).get(),stopRepository.findById(secondId).get());
        model.addAttribute("id",id);
        model.addAttribute("time",time);
        model.addAttribute("number",number);
        model.addAttribute("secondId",secondId);
        model.addAttribute("stops",stops);
        return "/en/searchResult.html";
    }

    @GetMapping("/buy/{id}/{secondId}")
    @PreAuthorize("hasAuthority('USER')")
    public String buyRoute(@PathVariable long id , @PathVariable long secondId,
                           Authentication authentication , Model model){
        LOGGER.info("Main controller: method 'buyRoute'");
        MyUser myUser = (MyUser) authentication.getPrincipal();
        Optional<User> userFound = userRepository.findById(myUser.getId());
        User user = userFound.get();
        Optional<Stop> byId1 = stopRepository.findById(id);
        Stop stop = byId1.get();
        List<Stop> allOfStops = stopRepository.findByTrain(stop.getTrain());
        if (allOfStops.get(1).getSeats()>0){
            stopService.buyTicket(user,allOfStops, id,secondId);
            return "redirect:/profile";
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
        LOGGER.info("Main controller: method 'sendHelp'");
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
        LOGGER.info("Main controller: method 'getRegistration'");
        model.addAttribute("user", new User());
        return "/en/registration.html";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute ("user") @Valid User user,
                          BindingResult bindingResult, Model model){
        LOGGER.info("Main controller: method 'addUser'");
        if (bindingResult.hasErrors()) return "/en/registration.html";
        if (!userService.addNewUser(user)) {
            model.addAttribute("exist", "exist");
        }
        return "/en/login.html";
    }
}