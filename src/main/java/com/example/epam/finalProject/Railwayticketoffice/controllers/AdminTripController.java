package com.example.epam.finalProject.Railwayticketoffice.controllers;

import com.example.epam.finalProject.Railwayticketoffice.data.StationsRepository;
import com.example.epam.finalProject.Railwayticketoffice.data.StopRepository;
import com.example.epam.finalProject.Railwayticketoffice.data.TicketRepository;
import com.example.epam.finalProject.Railwayticketoffice.data.UserRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Route;
import com.example.epam.finalProject.Railwayticketoffice.models.Stop;
import com.example.epam.finalProject.Railwayticketoffice.models.Ticket;
import com.example.epam.finalProject.Railwayticketoffice.services.StationService;
import com.example.epam.finalProject.Railwayticketoffice.services.StopService;
import com.example.epam.finalProject.Railwayticketoffice.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.*;

@Controller
@RequestMapping("/admin")
public class AdminTripController {

    TicketRepository ticketRepository;

    public AdminTripController(TicketRepository ticketRepository) {
        this.ticketRepository=ticketRepository;
    }

    @GetMapping("/trip")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getTrips(Model model){
        return "/admin/trip.html";
    }

    @GetMapping("/checkTrip")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String checkTrip(HttpServletRequest req,Model model){
        String tran =req.getParameter("tran");
        List <Ticket> tickets = ticketRepository.findAllByTran(tran);
        if (!tickets.isEmpty()) model.addAttribute("anything",tran);
        if (tickets.isEmpty()) model.addAttribute("nothing",tran);
        model.addAttribute("tickets", tickets);
        return "/admin/tripResult.html";
    }

    @GetMapping("/trip/delete/{tran}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getRouteDetails(@PathVariable String tran, Model model){
        List<Ticket> allByTran = ticketRepository.findAllByTran(tran);
        ticketRepository.deleteAll(allByTran);
        return "redirect:/admin/trip";
    }
}