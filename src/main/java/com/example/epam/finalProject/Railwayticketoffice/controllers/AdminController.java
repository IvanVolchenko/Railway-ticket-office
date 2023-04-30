package com.example.epam.finalProject.Railwayticketoffice.controllers;


import com.example.epam.finalProject.Railwayticketoffice.data.MessageRepository;
import com.example.epam.finalProject.Railwayticketoffice.data.UserRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Message;
import com.example.epam.finalProject.Railwayticketoffice.models.MyUser;
import com.example.epam.finalProject.Railwayticketoffice.models.Station;
import com.example.epam.finalProject.Railwayticketoffice.models.User;
import com.example.epam.finalProject.Railwayticketoffice.services.MessageService;
import com.example.epam.finalProject.Railwayticketoffice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * The main controller 'AdminController' com.example.epam.finalProject.Railwayticketoffice.controllers.
 * It's responsible for viewing and deleting users and statements.
 * @author Ivan Volchenko
 */
@Controller
public class AdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
    private final UserService userService;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final MessageService messageService;
    public AdminController(UserService userService, UserRepository userRepository,
                           MessageRepository messageRepository,MessageService messageService) {
        this.userService = userService;
        this.userRepository=userRepository;
        this.messageRepository=messageRepository;
        this.messageService=messageService;
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String startMethod(){
        return "/admin/main.html";
    }


    @GetMapping("/report")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getReport (Model model){
        return findAllUsers(1,model);
    }

    @GetMapping("/report/{pageNu}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String findAllUsers(@PathVariable (value = "pageNu") int pageNu,Model model){
        LOGGER.info("AdminController: method 'findAllUsers'");
        int size = 5;
        Page <User> page = userService.findAllUsers(pageNu,size);
        List<User> users = page.getContent();
        model.addAttribute("zero",1);
        model.addAttribute("currentPage",pageNu);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("users",users);
        return "/admin/report.html";
    }

    @GetMapping("/admin/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteProfile(@PathVariable("id") long id , Authentication authentication, Model model){
        MyUser user = (MyUser) authentication.getPrincipal();
        LOGGER.info("AdminController: method 'deleteProfile'");
        Optional<User> user1 = userRepository.findById(id);
        if (user1.isEmpty()) return "redirect:/admin";
        if (id==user.getId()) return "redirect:/admin";
        userRepository.deleteById(id);
        return "redirect:/report";
    }

    @GetMapping("/statements")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getStatements(Model model){
        LOGGER.info("AdminController: method 'getStatements'");
        return findAllStatements(1,model);    }

    @GetMapping("/statements/{pageNu}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String findAllStatements(@PathVariable (value = "pageNu") int pageNu,Model model){
        LOGGER.info("AdminController: method 'findAllStatements'");
        int size = 12;
        Page <Message> page = messageService.findAllMessages(pageNu,size);
        List<Message> messages = page.getContent();
        model.addAttribute("zero",1);
        model.addAttribute("currentPage",pageNu);
        model.addAttribute("totalPages",page.getTotalPages());
        model.addAttribute("totalItems",page.getTotalElements());
        model.addAttribute("messages",messages);
        return "/admin/statements.html";
    }

    @GetMapping("/statement/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteStatement (@PathVariable("id") long id, Model model) {
        LOGGER.info("AdminController: method 'deleteStatement'");
        Optional<Message> message = messageRepository.findById(id);
        if (message.isEmpty()) return "redirect:/statements";
        messageRepository.deleteById(id);
        return "redirect:/statements";
    }
}
