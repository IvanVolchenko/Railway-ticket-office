package com.example.epam.finalProject.Railwayticketoffice.services;

import com.example.epam.finalProject.Railwayticketoffice.data.TicketRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Message;
import com.example.epam.finalProject.Railwayticketoffice.models.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The interface TicketService com.example.epam.finalProject.Railwayticketoffice.services.
 * @author Ivan Volchenko
 */
@Service
public class TicketService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketService.class);
    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    public Page<Ticket> findAllTicketsByTran(String tran, int page, int size) {
        LOGGER.info("findAllTickets: method 'findAllUsers'");
        Pageable pageable = PageRequest.of(page - 1,size);
        return ticketRepository.findAllTicketsByTran(tran,pageable);
    }

    public Page<Ticket> findAllTicketsByDocument(String documentNumber, int pageNu, int size) {
        LOGGER.info("TicketService: method 'findAllTicketsByDocument'");
        Pageable pageable = PageRequest.of(pageNu - 1,size);
        return ticketRepository.findByDocument(documentNumber,pageable);
    }
}
