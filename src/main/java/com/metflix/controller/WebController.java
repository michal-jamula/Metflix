package com.metflix.controller;


import com.metflix.model.User;
import com.metflix.repositories.MovieRepository;
import com.metflix.repositories.UserRepository;
import com.metflix.service.AddressService;
import com.metflix.service.CreditCardService;
import com.metflix.service.MovieService;
import com.metflix.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Optional;


@Controller
public class WebController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final MovieRepository movieRepository;
    private final MovieService movieService;
    private final CreditCardService creditCardService;
    private final AddressService addressService;

    public WebController(UserRepository userRepository, UserService userService, MovieRepository movieRepository, MovieService movieService, CreditCardService creditCardService, AddressService addressService) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.movieRepository = movieRepository;
        this.movieService = movieService;
        this.creditCardService = creditCardService;
        this.addressService = addressService;
    }

    @GetMapping("/")
    public String index (Model model) {
        return "index";
    }

    @GetMapping("index")
    public String index () {
        return "index";
    }


    @GetMapping("login")
    public String login() {
        return "login";
    }



    @GetMapping("registration")
    public String registration(Model model, User user) {
        model.addAttribute("user", new User());
        return "registration";
    }

    /**
        Right now the form validation consists of:
            1. Checking whether the email is already registered
            2.  Checking if any fields are empty
            3. Checking if the passwords match

        Form currently does not check the Date entered, so bad format can still be passed through
     */
    @PostMapping("registration")
    public String registration(@ModelAttribute("user")Optional<User> userOptional, Model model, @ModelAttribute("password2")String password2) {

        if(userOptional.isEmpty()) {
            return "registration";
        }

        User user = userOptional.get();

        System.err.println(user);
        try {
            List<String> message = userService.validateUser(user, model.getAttribute("password2").toString());
            model.addAttribute(message.get(0), message.get(1));

            if (message.get(0).equals("success")) {
                userService.save(user);
                return "registration";
            } else {
                System.err.println("Success not received from userService, please check... " + message.get(0) + " " + message.get(1) );
            }
        }

        catch (Exception e) {
            System.out.println(e);
            System.err.println("Unknown error during registration, please fix ASAP!!");
            return "registration";
        }

        return "registration";
    }





}