package com.example.epam.finalProject.Railwayticketoffice.services;

import com.example.epam.finalProject.Railwayticketoffice.data.MessageRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * The interface MessageService com.example.epam.finalProject.Railwayticketoffice.services.
 * @author Ivan Volchenko
 */
@Service
public class MessageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Page<Message> findAllMessages(int page, int size) {
        LOGGER.info("findAllTickets: method 'findAllUsers'");
        Pageable pageable = PageRequest.of(page - 1,size);
        return messageRepository.findAll(pageable);
    }
}
