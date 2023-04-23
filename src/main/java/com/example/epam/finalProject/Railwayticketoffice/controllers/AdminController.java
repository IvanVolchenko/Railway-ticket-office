package com.example.epam.finalProject.Railwayticketoffice.controllers;


import com.example.epam.finalProject.Railwayticketoffice.data.MessageRepository;
import com.example.epam.finalProject.Railwayticketoffice.data.UserRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Message;
import com.example.epam.finalProject.Railwayticketoffice.models.User;
import com.example.epam.finalProject.Railwayticketoffice.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The main controller 'AdminController' com.example.epam.finalProject.Railwayticketoffice.controllers.
 * It's responsible for viewing and deleting users and statements.
 * @author Ivan Volchenko
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminController.class);
    UserService userService;
    UserRepository userRepository;
    MessageRepository messageRepository;
    public AdminController(UserService userService, UserRepository userRepository,
                           MessageRepository messageRepository) {
        this.userService = userService;
        this.userRepository=userRepository;
        this.messageRepository=messageRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String startMethod(){
        return "/admin/main.html";
    }

    @GetMapping("/report")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String findAllUsers(Model model){
        LOGGER.info("AdminController: method 'findAllUsers'");
        ArrayList<User> users= userService.findAllUsers();
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o1.getLastName().compareTo(o2.getLastName());
            }
        });
        model.addAttribute("users",users);
        return "/admin/report.html";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteProfile(@PathVariable("id") long id , Authentication authentication, Model model){
        LOGGER.info("AdminController: method 'deleteProfile'");
        if (id==1) return "redirect:/admin";
        userRepository.deleteById(id);
        return "redirect:/admin/report";
    }

    @GetMapping("/statements")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getStatements(Model model){
        LOGGER.info("AdminController: method 'getStatements'");
        ArrayList <Message>  statements = (ArrayList<Message>) messageRepository.findAll();
        Collections.sort(statements, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return (int) (o1.getId()-o2.getId());
            }
        });
        model.addAttribute("statements",statements);
        return "/admin/statements.html";
    }
    @GetMapping("/statement/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteStatement (@PathVariable("id") long id, Model model) {
        LOGGER.info("AdminController: method 'deleteStatement'");
        messageRepository.deleteById(id);
        return "redirect:/admin/statements";
    }
}
