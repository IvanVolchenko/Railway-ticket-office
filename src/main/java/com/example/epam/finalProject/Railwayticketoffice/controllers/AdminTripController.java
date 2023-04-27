package com.example.epam.finalProject.Railwayticketoffice.controllers;

import com.example.epam.finalProject.Railwayticketoffice.data.TicketRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Ticket;
import com.example.epam.finalProject.Railwayticketoffice.services.TicketService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;

/**
 * The controller 'AdminTripController' com.example.epam.finalProject.Railwayticketoffice.controllers.
 * It's responsible for the page viewing and deleting tickets for a specific route.
 * @author Ivan Volchenko
 */
@Controller
public class AdminTripController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminTripController.class);
    private final TicketRepository ticketRepository;
    private final TicketService ticketService;

    public AdminTripController(TicketRepository ticketRepository,TicketService ticketService) {
        this.ticketRepository=ticketRepository;
        this.ticketService=ticketService;
    }

    @GetMapping("/trip")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getTrips(Model model){
        return "/admin/trip.html";
    }

    @GetMapping("/checkTrip")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String checkTrip(HttpServletRequest req,Model model){
        LOGGER.info("AdminTripController: method 'checkTrip'");
        String tran =req.getParameter("tran");
        return checkTickets(tran,1,model);
    }

    @GetMapping("/checkTrip/{tran}/{pageNu}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String checkTickets(@PathVariable (value = "tran") String tran,
                               @PathVariable (value = "pageNu") int pageNu,Model model){
        LOGGER.info("AdminTripController: method 'checkTrip'");
        int size = 12;
        Page<Ticket> allTicketsByTrain = ticketService.findAllTicketsByTran(tran, pageNu, size);
        if (!allTicketsByTrain.isEmpty()) model.addAttribute("anything",tran);
        if (allTicketsByTrain.isEmpty()) model.addAttribute("nothing",tran);
        model.addAttribute("tran",tran);
        model.addAttribute("zero",1);
        model.addAttribute("currentPage",pageNu);
        model.addAttribute("totalPages",allTicketsByTrain.getTotalPages());
        model.addAttribute("totalItems",allTicketsByTrain.getTotalElements());
        model.addAttribute("tickets", allTicketsByTrain);
        return "/admin/tripResult.html";
    }

    @GetMapping("/trip/delete/{tran}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getRouteDetails(@PathVariable String tran, Model model){
        LOGGER.info("AdminTripController: method 'getRouteDetails'");
        List<Ticket> allByTran = ticketRepository.findAllByTran(tran);
        ticketRepository.deleteAll(allByTran);
        return "redirect:/trip";
    }
}
