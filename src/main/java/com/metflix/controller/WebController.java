package com.metflix.controller;



import com.metflix.auth.AuthenticationRequest;
import com.metflix.model.Authority;
import com.metflix.model.User;
import com.metflix.repositories.AuthorityRepository;
import com.metflix.repositories.MovieRepository;
import com.metflix.repositories.UserRepository;
import com.metflix.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class WebController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final MovieRepository movieRepository;
    private final MovieService movieService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index (Model model) {
        return "index";
    }

    @GetMapping("index")
    public String index () {
        return "index";
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
    public String registerUser(@ModelAttribute("user")Optional<User> userOptional, Model model, @ModelAttribute("password2")String password2) {

        if(userOptional.isEmpty()) {
            return "registration";
        }

        User user = userOptional.get();

        System.err.println(user);
        try {
            List<String> message = userService.validateUser(user, model.getAttribute("password2").toString());
            model.addAttribute(message.get(0), message.get(1));

            if (message.get(0).equals("success")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userService.save(user);
                return "registration";
            } else {
                System.err.println("Success not received from userService, please check... " + message.get(0) + " " + message.get(1) );
            }
        }

        catch (Exception e) {
            System.out.println(e);
            System.err.println("Error during registration, please check");
            return "registration";
        }

        return "registration";
    }




    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute(new AuthenticationRequest());
        return "login";
    }


//    @RequestMapping(value = "login", method = RequestMethod.POST)
//    public String login (@RequestBody AuthenticationRequest authenticationRequest) {
//        System.err.println("POST LOGIN");
//        System.out.println("user tried to login, here's the details: " + authenticationRequest);
//        return "index";
//    }






}

