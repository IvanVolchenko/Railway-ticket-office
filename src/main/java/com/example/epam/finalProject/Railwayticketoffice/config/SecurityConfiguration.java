package com.example.epam.finalProject.Railwayticketoffice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfiguration{

    @Autowired
    private DataSource dataSource;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .authorizeHttpRequests(authConfig ->{
                    authConfig.requestMatchers("/","/registration","/help", "/images/general/**").permitAll();
                    authConfig.requestMatchers("/admin/**","/images/admin/**").hasAuthority("ADMIN");// hasRole("ADMIN");
                    authConfig.anyRequest().authenticated();
                })
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/logout-success").permitAll();
        return http.build();
    }

    @Bean
    UserDetailsService myUserDetailsService (){
        return new MyUserDetailsService();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder(){
        return new  BCryptPasswordEncoder(11);
    }

    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> successEvent(){
        return event -> {
            System.err.println("Success Login:");
        };
    }

    @Bean
    public ApplicationListener<AuthenticationFailureBadCredentialsEvent> failureEvent(){
        return event -> {
            System.err.println("Bad Login:");
        };
    }


}
