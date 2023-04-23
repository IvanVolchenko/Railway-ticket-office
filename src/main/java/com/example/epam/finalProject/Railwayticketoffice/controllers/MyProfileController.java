package com.example.epam.finalProject.Railwayticketoffice.controllers;

import com.example.epam.finalProject.Railwayticketoffice.data.TicketRepository;
import com.example.epam.finalProject.Railwayticketoffice.data.UserRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.MyUser;
import com.example.epam.finalProject.Railwayticketoffice.models.Ticket;
import com.example.epam.finalProject.Railwayticketoffice.models.User;
import com.example.epam.finalProject.Railwayticketoffice.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;

/**
 * The controller 'MyProfileController' com.example.epam.finalProject.Railwayticketoffice.controllers.
 * It's responsible for the profile page.
 * @author Ivan Volchenko
 */
@Controller
public class MyProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyProfileController.class);
    UserService userService;
    UserRepository userRepository;
    TicketRepository ticketRepository;

    public MyProfileController(UserService userService, UserRepository userRepository
            ,TicketRepository ticketRepository) {
        this.userService = userService;
        this.userRepository=userRepository;
        this.ticketRepository=ticketRepository;
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN')")
    public String getProfile (Authentication authentication, Model model) {
        LOGGER.info("MyProfileController: method 'getProfile'");
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        User userFinal = user.get();
        if (userFinal.getAuthorities().equals("USER")) model.addAttribute("bott","bott");
        List<Ticket> tickets = ticketRepository.findByDocument(userFinal.getDocumentNumber());
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

    @PostMapping("/changes")
    @PreAuthorize("hasAuthority('USER')")
    public String saveProfile(@ModelAttribute("user") @Valid User user,
                              BindingResult bindingResult, Authentication authentication, Model model){
        LOGGER.info("MyProfileController: method 'saveProfile'");
        if (bindingResult.hasErrors()) return "/en/change.html";
        MyUser myUser = (MyUser) authentication.getPrincipal();
        user.setId(myUser.getId());
        user.setUsername(user.getEmailAddress());
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
