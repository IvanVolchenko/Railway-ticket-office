package com.example.epam.finalProject.Railwayticketoffice.services;

import com.example.epam.finalProject.Railwayticketoffice.data.UserRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

/**
 * The interface UserService com.example.epam.finalProject.Railwayticketoffice.services.
 * @author Ivan Volchenko
 */
@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private  UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean addNewUser (User user){
        LOGGER.info("UserService: method 'addNewUser'");
        Optional<User> userCheck = userRepository.findByEmailAddress(user.getEmailAddress());
        Optional<User> userCheck2 = userRepository.findByDocumentNumber(user.getDocumentNumber());
        if (userCheck.isPresent() || userCheck2.isPresent()) {
            return false;
        }
        user.setAuthorities("USER");
        user.setUsername(user.getEmailAddress());
        String pas = user.getPassword();
        user.setPassword(passwordEncoder.encode(pas));
        userRepository.save(user);
        return true;
    }

    public boolean change (User user){
        LOGGER.info("UserService: method 'change'");
        Optional<User> userCheck = userRepository.findByEmailAddress(user.getEmailAddress());
        Optional<User> userCheck2 = userRepository.findByDocumentNumber(user.getDocumentNumber());
        boolean first= userCheck.isPresent();
        boolean second= userCheck2.isPresent();
        User user1;
        User user2;
        if (first && second){
            user1 = userCheck.get();
            user2 = userCheck2.get();
            if (user.getId()!=user1.getId() || user1.getId()!=user2.getId()){
                return false;
            }
        }
        if (first){
            user1 = userCheck.get();
            if (user.getId()!=user1.getId())return false;
        }
        if (second){
            user2 = userCheck2.get();
            if (user.getId()!=user2.getId())return false;
        }
        String pas = user.getPassword();
        user.setPassword(passwordEncoder.encode(pas));
        userRepository.save(user);
        return true;
    }

    public Page<User> findAllUsers(int page, int size) {
        LOGGER.info("UserService: method 'findAllUsers'");
        Pageable pageable = PageRequest.of(page - 1,size);
        return userRepository.findAll(pageable);
    }


}
