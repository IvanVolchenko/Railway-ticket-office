package com.example.epam.finalProject.Railwayticketoffice.controllers;


import com.example.epam.finalProject.Railwayticketoffice.data.UserRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.User;
import com.example.epam.finalProject.Railwayticketoffice.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping("/admin")
public class AdminController {

    UserService userService;
    UserRepository userRepository;
    public AdminController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository=userRepository;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String startMethod(){
        return "/admin/main.html";
    }

    @GetMapping("/report")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String findAllUsers(Model model){
        ArrayList<User> users= userService.findAllUsers();
        model.addAttribute("users",users);
        return "/admin/report.html";
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteProfile(@PathVariable("id") long id , Authentication authentication, Model model){
        if (id==1) return "redirect:/admin";
        userRepository.deleteById(id);
        return "redirect:/admin/report";
    }
}
