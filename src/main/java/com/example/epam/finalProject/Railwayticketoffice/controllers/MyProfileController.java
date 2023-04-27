package com.example.epam.finalProject.Railwayticketoffice.controllers;

import com.example.epam.finalProject.Railwayticketoffice.data.UserRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.MyUser;
import com.example.epam.finalProject.Railwayticketoffice.models.Ticket;
import com.example.epam.finalProject.Railwayticketoffice.models.User;
import com.example.epam.finalProject.Railwayticketoffice.services.TicketService;
import com.example.epam.finalProject.Railwayticketoffice.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * The controller 'MyProfileController' com.example.epam.finalProject.Railwayticketoffice.controllers.
 * It's responsible for the profile page.
 * @author Ivan Volchenko
 */
@Controller
public class MyProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyProfileController.class);
    private final UserService userService;
    private final UserRepository userRepository;
    private final TicketService ticketService;

    public MyProfileController(UserService userService, UserRepository userRepository,
                               TicketService ticketService) {
        this.userService = userService;
        this.userRepository=userRepository;
        this.ticketService=ticketService;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public String getProfile (Authentication authentication, Model model) {
        LOGGER.info("MyProfileController: method 'getProfile'");
        return getPageProfile(1,authentication,model);
    }

    @GetMapping("/profile/{pageNu}")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public String getPageProfile (@PathVariable(value = "pageNu") int pageNu,
                                  Authentication authentication, Model model) {
        LOGGER.info("MyProfileController: method 'getPageProfile'");
        int size = 4;
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        User userFinal = user.get();
        Page<Ticket> page = ticketService.findAllTicketsByDocument(userFinal.getDocumentNumber(),pageNu,size);
        List <Ticket> ticketList = page.getContent();
        ArrayList<Ticket> tickets = new ArrayList<>(ticketList);
        tickets.sort(Comparator.comparing(Ticket::getDate));
        model.addAttribute("zero",1);
        model.addAttribute("currentPage",pageNu);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("tickets",tickets);
        model.addAttribute("user",userFinal);
        return "/en/profile.html";
    }


    @GetMapping("/change")
    @PreAuthorize("hasAuthority('USER')")
    public String changeProfile(Authentication authentication, Model model){
        LOGGER.info("MyProfileController: method 'changeProfile'");
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        User userFinal = user.get();
        model.addAttribute("user",userFinal);
        return "/en/change.html";
    }

    @GetMapping("/changeAdmin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String changeAdminProfile(){
        LOGGER.info("MyProfileController: method 'changeAdminProfile'");
        return "/en/changeAdmin.html";
    }

    @PostMapping("/changesAdmin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveAdminProfile(@RequestParam String firstName, @RequestParam String lastName,
                                   @RequestParam String password, Authentication authentication, Model model){
        LOGGER.info("MyProfileController: method 'saveAdminProfile'");
        MyUser myUser = (MyUser) authentication.getPrincipal();
        Optional<User> admin = userRepository.findById(myUser.getId());
        User user = admin.get();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPassword(password);
        user.setUsername(user.getEmailAddress());
        user.setAuthorities("ADMIN");
        userService.change(user);
        return "redirect:/logout";
    }

    @PostMapping("/changes")
    @PreAuthorize("hasAuthority('USER')")
    public String saveProfile(@ModelAttribute("user") @Valid User user,
                              BindingResult bindingResult, Authentication authentication, Model model){
        LOGGER.info("MyProfileController: method 'saveProfile'");
        if (bindingResult.hasErrors()) return "/en/change.html";
        MyUser myUser = (MyUser) authentication.getPrincipal();
        user.setId(myUser.getId());
        user.setUsername(user.getEmailAddress());
        user.setAuthorities("USER");
        if (!userService.change(user)) {
            model.addAttribute("exist", "exist");
            return "/en/change.html";
        }
        return "redirect:/logout";
    }

    @GetMapping("/delete")
    @PreAuthorize("hasAuthority('USER')")
    public String deleteProfile(Authentication authentication, Model model){
        LOGGER.info("MyProfileController: method 'deleteProfile'");
        MyUser myUser = (MyUser) authentication.getPrincipal();
        userRepository.deleteById(myUser.getId());
        return "redirect:/logout";
    }
}
