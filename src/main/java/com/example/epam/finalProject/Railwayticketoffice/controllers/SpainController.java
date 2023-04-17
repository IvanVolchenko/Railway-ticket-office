package com.example.epam.finalProject.Railwayticketoffice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sp")
public class SpainController {

    @GetMapping()
    public String startSpain (){
        return "sp/index-sp.html";
    }

}
