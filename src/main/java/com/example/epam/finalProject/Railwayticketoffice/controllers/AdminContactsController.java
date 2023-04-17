package com.example.epam.finalProject.Railwayticketoffice.controllers;

import com.example.epam.finalProject.Railwayticketoffice.data.ContactsRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.Contact;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminContactsController {

    ContactsRepository contactsRepository;

    public AdminContactsController(ContactsRepository contactsRepository ) {
        this.contactsRepository= contactsRepository;
    }
    @GetMapping("/contacts")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getContacts (Model model) {
        Iterable <Contact> contacts = contactsRepository.findAll();
        model.addAttribute("contacts", contacts);
        return "/admin/contacts.html";
    }

    @PostMapping("/addContacts")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addContacts (@RequestParam String name, @RequestParam String place,
                               @RequestParam String address, @RequestParam String email,
                               @RequestParam String phone, Model model) {
        Contact contact = new Contact(name,place,address,email,phone);
        contactsRepository.save(contact);
        return "redirect:/admin/contacts";
    }



    @GetMapping("/contacts/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteContact (@PathVariable("id") long id, Model model) {
        contactsRepository.deleteById(id);
        return "redirect:/admin/contacts";
    }
}
