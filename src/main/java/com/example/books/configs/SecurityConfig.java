package com.example.books.configs;

import com.example.books.controllers.AuthorController;
import com.example.books.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

import org.springframework.web.filter.OncePerRequestFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final Logger LOGGER= LoggerFactory.getLogger(SecurityConfig.class);

    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    // User Creation
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder) {
//        // InMemoryUserDetailsManager setup with two users
//        UserDetails admin = User.withUsername("Amiya")
//                .password(encoder.encode("123"))
//                .roles("ADMIN", "USER")
//                .build();
//
//        UserDetails user = User.withUsername("Ejaz")
//                .password(encoder.encode("123"))
//                .roles("USER")
//                .build();
//
//        return new InMemoryUserDetailsManager(admin, user);
//    }

    // Configuring HttpSecurity
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        LOGGER.info("security filter chain");
//        http
//                .csrf(csrf -> csrf.disable()) // Disable CSRF for simplicity
//                .authorizeHttpRequests(auth -> auth
//                         // Permit all access to /auth/welcome
//                        .requestMatchers("/users/profile").authenticated() // Require authentication for /auth/user/**
//                      .requestMatchers("/users/signUp").permitAll()
//                        .requestMatchers("/books/**").authenticated()
//                        .requestMatchers("/author/**").authenticated()
//                        .requestMatchers("/users/logIn").permitAll()
//                );
////                .formLogin(form -> form
////                .loginPage("/users/logIn") // Custom login page
////                .permitAll()
//
//
//        return http.build();
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        return http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//        Set permissions on endpoints
                .authorizeHttpRequests(auth -> auth
//            our public endpoints
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/signUp/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/logIn/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/books/allBooks").permitAll()
                        .requestMatchers(HttpMethod.POST, "/books/allBooks").permitAll()
                                .requestMatchers(HttpMethod.POST, "/books/allBooks/sort").permitAll()
                        .requestMatchers(HttpMethod.GET, "/books/{id}/details").permitAll()
                        .requestMatchers(HttpMethod.GET, "/author/allAuthors").permitAll()
                        .requestMatchers(HttpMethod.POST, "/author/allAuthors").permitAll()
                        .requestMatchers(HttpMethod.POST, "/author/allAuthors/sort").permitAll()
                        .requestMatchers(HttpMethod.GET, "/author/{id}/details").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/profile").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/profile").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/logout").permitAll()
                        .requestMatchers(HttpMethod.GET, "/buy/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/buy/**").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/buy/**").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/buy/**").permitAll()
                      //  .requestMatchers(HttpMethod.POST,"").permitAll()
                        ///{id}/edit
//            our private endpoints
                        .requestMatchers(HttpMethod.POST, "/books/addBook").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/books/addBook").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/books/{id}/deleteBook").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/books/{id}/deleteBook").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/books/{id}/deleteBook").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/books/{id}/edit").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/books/{id}/edit").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/author/addAuthor").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/author/{id}/deleteAuthor").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/author/{id}/deleteAuthor").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/author/{id}/deleteAuthor").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/author/{id}/edit").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/author/{id}/edit").hasRole("ADMIN")

                        ///buy/update/${id}
                        .anyRequest().authenticated())

                .authenticationManager(authenticationManager)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}