package com.example.epam.finalProject.Railwayticketoffice.config;

import com.example.epam.finalProject.Railwayticketoffice.data.UserRepository;
import com.example.epam.finalProject.Railwayticketoffice.models.MyUser;
import com.example.epam.finalProject.Railwayticketoffice.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.HashSet;
import java.util.Set;

public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional <User> userByUsername = userRepository.findByUsername(username);

        if (!userByUsername.isPresent()) {
            throw  new UsernameNotFoundException("Invalid credentials!");
        }
        User user = userByUsername.get();

        if (user==null || !user.getUsername().equals(username)) {
            throw  new UsernameNotFoundException("Invalid credentials!");
        }

        Set <GrantedAuthority> grantedAuthorities = new HashSet<>();
        grantedAuthorities.add(new SimpleGrantedAuthority(user.getAuthorities()));

        return new MyUser(user.getUsername(),user.getPassword(),true,true,
                true, true,grantedAuthorities,user.getId() ,user.getFirstName(),
                user.getLastName(),user.getDocumentNumber(),user.getEmailAddress());
    }
}
