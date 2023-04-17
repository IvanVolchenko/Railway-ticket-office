package com.example.epam.finalProject.Railwayticketoffice.services;

import com.example.epam.finalProject.Railwayticketoffice.data.UserRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    private  UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean addNewUser (User user){
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
        user.setAuthorities("USER");
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

    public ArrayList<User> findAllUsers() {
        Iterable <User> users = userRepository.findAll();
        ArrayList<User> usersWithoutAdmin = new ArrayList<>();
        users.forEach(user -> {
            if (!user.getAuthorities().equals("ADMIN")) {
                usersWithoutAdmin.add(user);
            }
        });
        return usersWithoutAdmin;
    }
}
