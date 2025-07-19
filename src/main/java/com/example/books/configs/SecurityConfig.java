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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {

        return http
                .csrf().disable()
                .authorizeHttpRequests(auth -> auth
//            our public endpoints
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/signUp/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/signUp/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/logIn/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/logIn/**").permitAll()

                        .requestMatchers(HttpMethod.POST, "/books/addBook").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/books/addBook").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/books/{id}/deleteBook").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/books/{id}/deleteBook").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/books/{id}/deleteBook").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/books/{id}/edit").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/books/{id}/edit").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/author/addAuthor").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/author/{id}/deleteAuthor").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/author/{id}/deleteAuthor").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/author/{id}/deleteAuthor").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/author/{id}/edit").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/author/{id}/edit").hasAuthority("ADMIN")

                        ///buy/update/${id}
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/users/logIn")                    // GET - show login form
                        .loginProcessingUrl("/users/logIn")                // POST - form submits here
                        .defaultSuccessUrl("/books/allBooks", true)   // Redirect after successful login
                        .failureUrl("/users/logIn?error=true")        // Redirect on failed login
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/users/logout")
                        .logoutSuccessUrl("/users/logIn?logout=true")
                        .permitAll()
                )
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}