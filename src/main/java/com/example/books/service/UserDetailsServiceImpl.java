package com.example.books.service;

import com.example.books.controllers.UserController;
import com.example.books.exceptions.NotFoundException;
import com.example.books.models.User;
import com.example.books.repos.UserRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final Logger LOGGER= LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserRepo repository;

    public UserDetailsServiceImpl(UserRepo repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        LOGGER.info("inside load by username");
        User user = repository.findByUsername(username).orElseThrow(() ->
                new NotFoundException(String.format("User does not exist, username: %s", username)));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
    }
}