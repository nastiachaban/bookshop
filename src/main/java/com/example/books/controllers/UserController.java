package com.example.books.controllers;

import com.example.books.helper.JwtHelper;
import com.example.books.models.*;
import com.example.books.service.AuthorService;
import com.example.books.service.BuyService;
import com.example.books.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import net.bytebuddy.utility.RandomString;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.regex.Pattern;


@Controller
@RequestMapping("/users")
public class UserController {
    private final Logger LOGGER= LoggerFactory.getLogger(UserController.class);
     @Autowired
     private AuthenticationManager authenticationManager;
    @Autowired
    private JavaMailSender emailSender;
    @Autowired
    private UserService service;

    @Autowired
    private BuyService service2;

    @GetMapping("/logIn")
    public String logIn(Model model){
        model.addAttribute("user",new LogIn());
        LOGGER.info("redirect to login page");
        return "login";
    }

    @PostMapping("/signUp")
    public String signUp(@ModelAttribute SignUp requestDto) {
        LOGGER.info("post signup");
        service.signup(requestDto);
        // TODO
        return "redirect:/users/logIn";
        //return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/signUp")
    public String signUp(Model model){
        LOGGER.info("get signup");
        model.addAttribute("user",new SignUp());
        return "signup";
    }

    @PostMapping("/logout")
    public String logout(){
        SecurityContextHolder.clearContext();
        return "redirect:/users/logIn";
    }

//    @PostMapping("/signUp")
//    public String createUser(Model model, SignUp user, BindingResult result){
//        if(result.hasErrors()){
//            model.addAttribute("user", user);
//            LOGGER.error("binding result has errors");
//            return "signup";
//        }
//
//        if(service.existsByUsername(user.getUsername())){
//            LOGGER.error("user with the same username already exists");
//            user.setInfo("user with the same username already exists");
//            model.addAttribute("user", user);
//            return "signup";
//        }
//
//        if(!user.getPassword().equals(user.getConfirmPassword())){
//            LOGGER.error("passwords are not the same");
//            user.setInfo("passwords are not the same");
//            model.addAttribute("user", user);
//            return "signup";
//        }
//
//        User u=new User(user.getFirstname(),user.getLastname(),user.getUsername(),service.hashPassword(user.getPassword()),user.getEmail(),false,RandomString.make(64), Role.USER);
//        service.createUser(u);
//        SimpleMailMessage message=new SimpleMailMessage();
//        message.setFrom("bookstore");
//        message.setTo(u.getEmail());
//        message.setSubject("account confirmation");
//        message.setText("PLease follow the link below to activate your account. http://localhost:8080/users/verifyAccount/"+u.getVerifiedCode());
//        emailSender.send(message);
//        LOGGER.info("user was successfully created");
//        return "redirect:/users/logIn";
//    }

    @GetMapping("/verifyAccount/{code}")
    public String verify(@PathVariable String code){
        if(service.verifyUser(code)){
            return "accountVerified";
        }
        else {
            return "error";
        }
    }


    @GetMapping("/profile")
    public String profile(Model model, @AuthenticationPrincipal User user){
        LOGGER.info("redirect to profile page");
        model.addAttribute("user",user);
        return "profile";
    }

    @PostMapping("/profile")
    public String myProfile(Model model, User user, BindingResult result) {
        if (result.hasErrors()) {
            return "profile";
        }
        String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" +
                "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

        boolean emailCheck = Pattern.compile(regexPattern).matcher(user.getEmail()).matches();

        String errorMessage = " ";
        if (user.getFirstname().isEmpty() || user.getLastname().isEmpty() || user.getUsername().isEmpty() || user.getPhoneNumber().isEmpty()|| user.getEmail().isEmpty()) {
            errorMessage = "One of the fields is empty";
            model.addAttribute("error", errorMessage);
            model.addAttribute("user", user);
            return "profile";
        }

        if (!emailCheck) {
            errorMessage = "Please enter a valid email address";
            model.addAttribute("error", errorMessage);
            model.addAttribute("user", user);
            return "profile";
        }

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof User currentUser) {
            currentUser.setFirstname(user.getFirstname());
            currentUser.setLastname(user.getLastname());
            currentUser.setUsername(user.getUsername());
            currentUser.setEmail(user.getEmail());
            currentUser.setPhoneNumber(user.getPhoneNumber());

            service.updateUser(currentUser);

            model.addAttribute("success", "Profile updated");
        }

        return "redirect:/books/allBooks";


//        if (!service.existsByUsername(user.getUsername())) {
//            currentUser.setUsername(user.getUsername());
//            currentUser.setEmail(user.getEmail());
//            currentUser.setLastname(user.getLastname());
//            currentUser.setPhoneNumber(user.getPhoneNumber());
//            service.updateUser(currentUser);
//        }
//        return "profile";
    }

    @GetMapping("/changeP")
    public String changeP(Model model){
        LOGGER.info("redirect to change password page");
        model.addAttribute("passwords", new ChangePassword());
        return "changeP";
    }

    @PostMapping("/changeP")
    public String changePassword(Model model, ChangePassword passwords, BindingResult result, @AuthenticationPrincipal User user) {
        if (result.hasErrors()) {
            return "changeP";
        }
        if (passwords.getPassword().isEmpty() || passwords.getNewPassword().isEmpty() || passwords.getConfirmPassword().isEmpty()) {
            model.addAttribute("error", "All fields must be filled out");
            model.addAttribute("passwords", passwords);
            return "changeP";
        }
        if (!passwords.getNewPassword().equals(passwords.getConfirmPassword())) {
            model.addAttribute("error", "New password and confirm password do not match");
            model.addAttribute("passwords", passwords);
            return "changeP";
        }
        if (!user.getPassword().equals(service.hashPassword(passwords.getPassword()))) {
            model.addAttribute("error", "Old password is incorrect");
            model.addAttribute("passwords", passwords);
            return "changeP";
        }
        service.savePassword(user, passwords.getNewPassword());

        return "redirect:/users/profile";
    }

  /*  @GetMapping("/confirmCode")
    public String confirmCode(Model model){
        model.addAttribute("error","verification code is sent to your email");
        user.setConfirmCode(RandomString.make(8));
        user.setConfirmDate(LocalDateTime.now());
        service.updateUser(user);
        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("bookstore");
        message.setTo(user.getEmail());
        message.setSubject("changing password");
        message.setText("Verification code: "+user.getConfirmCode());
        emailSender.send(message);
        LOGGER.info("get mapping for confirm code");
        return "changePasswordVerification";
    }

    @PostMapping("/confirmCode")
    public String confirmationCode(Model model,@RequestParam(value = "confirmCode", required = true) String confirmCode){
        LOGGER.info("post mapping for confirm code");
        Duration d = Duration.between(user.getConfirmDate(), LocalDateTime.now());
        long seconds = d.toSeconds();
        if(confirmCode.equals(user.getConfirmCode()) && seconds<300){
            return "redirect:/users/changeP";
        }
       else{
            model.addAttribute("error","verification code is expired");
            return "changePasswordVerification";
        }
    }
*/


}
