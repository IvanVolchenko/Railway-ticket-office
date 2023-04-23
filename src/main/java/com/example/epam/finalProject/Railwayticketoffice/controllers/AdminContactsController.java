package com.example.epam.finalProject.Railwayticketoffice.controllers;

import com.example.epam.finalProject.Railwayticketoffice.data.ContactsRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Contact;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * The  controller 'AdminContactsController' com.example.epam.finalProject.Railwayticketoffice.controllers.
 * It's responsible for viewing and deleting useful contacts.
 * @author Ivan Volchenko
 */
@Controller
@RequestMapping("/admin")
public class AdminContactsController {

    ContactsRepository contactsRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(AdminContactsController.class);

    public AdminContactsController(ContactsRepository contactsRepository ) {
        this.contactsRepository= contactsRepository;
    }
    @GetMapping("/contacts")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getContacts (Model model) {
        LOGGER.info("AdminContactsController: method 'getContacts'");
        Iterable <Contact> contacts = contactsRepository.findAll();
        model.addAttribute("contacts", contacts);
        return "/admin/contacts.html";
    }

    @PostMapping("/addContacts")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addContacts (@RequestParam String name, @RequestParam String place,
                               @RequestParam String address, @RequestParam String email,
                               @RequestParam String phone, Model model) {
        LOGGER.info("AdminContactsController: method 'addContacts'");
        Contact contact = new Contact(name,place,address,email,phone);
        contactsRepository.save(contact);
        return "redirect:/admin/contacts";
    }



    @GetMapping("/contacts/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteContact (@PathVariable("id") long id, Model model) {
        LOGGER.info("AdminContactsController: method 'deleteContact'");
        contactsRepository.deleteById(id);
        return "redirect:/admin/contacts";
    }
}
