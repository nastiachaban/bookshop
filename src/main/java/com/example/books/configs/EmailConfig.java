package com.example.books.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailConfig {

//    @Bean
//    public JavaMailSenderImpl getJavaMailSender(){
//        JavaMailSenderImpl sender=new JavaMailSenderImpl();
//        sender.setHost("smtp.gmail.com");
//        sender.setPort(465);
//        sender.setUsername("cabananastasia51@gmail.com");
//        sender.setPassword("chaban2020!");
//        Properties props = sender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//
//
//        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
//
//        return sender;
//    }
}
