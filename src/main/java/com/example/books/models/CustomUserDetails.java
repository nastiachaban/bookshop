package com.example.books.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {
    private Long userID;
    private String username;
    private String password;
    private Role role;

    public CustomUserDetails(Long userID, String username, String password, Role role) {
        super();
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Long getUserID(){
        return this.userID;
    }

    public Role getRole(){
        return this.role;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }


    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }


    @Override
    public boolean isEnabled() {
        return true;
    }

    public static UserDetails create(User entity) {
        return new CustomUserDetails(entity.getId(), entity.getUsername(), entity.getPassword(), entity.getRole());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();

        list.add(new SimpleGrantedAuthority("ROLE_" + role));

        return list;
    }
    @Override
    public String getUsername() {
        return username;
    }}