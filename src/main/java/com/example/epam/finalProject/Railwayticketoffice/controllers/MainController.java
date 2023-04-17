package com.example.epam.finalProject.Railwayticketoffice.controllers;

import com.example.epam.finalProject.Railwayticketoffice.data.UserRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.MyUser;
import com.example.epam.finalProject.Railwayticketoffice.models.User;
import com.example.epam.finalProject.Railwayticketoffice.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("")
public class MainController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    UserService userService;
    UserRepository userRepository;

    public MainController(UserService userService, UserRepository userRepository ) {
        this.userService = userService;
        this.userRepository=userRepository;
    }

    @GetMapping("/")
    public String startMethod(Authentication authentication,Model model){
        return "/en/index.html";
    }

    // TODO удалить
    @GetMapping("/temp")
    public String tempMethod(Authentication authentication, Model model){
        MyUser myUser = (MyUser) authentication.getPrincipal();
        model.addAttribute("name", authentication.getAuthorities());
        return "/en/welcome.html";
    }

    @GetMapping("/help")
    public String startHelp(){
        return "/en/help.html";
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