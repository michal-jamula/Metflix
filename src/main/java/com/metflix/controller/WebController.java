package com.metflix.controller;



import com.metflix.auth.AuthenticationRequest;
import com.metflix.exceptions.EmailAlreadyExistsException;
import com.metflix.exceptions.PasswordsDontMatchException;
import com.metflix.exceptions.UserFieldIsEmptyException;
import com.metflix.model.User;
import com.metflix.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.websocket.server.PathParam;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class WebController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String index () {
        return "index";
    }

    @GetMapping("index")
    public String mainIndex () {
        return "index";
    }


    @GetMapping("registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    /**
        Right now the form validation consists of: <br>
            1. Checking whether the email is already registered <br>
            2.  Checking if any fields are empty <br>
            3. Checking if the passwords match
     */
    @PostMapping("registration")
    public String registerUser(@ModelAttribute("user")Optional<User> userOptional, Model model, @ModelAttribute("password2")String password2) {


        if(userOptional.isEmpty()) {
            System.err.println("Received empty user during registration!");
            model.addAttribute("message", "Received empty user. Try again");
            return "error";
        }

        User user = userOptional.get();

        try {
            userService.validateUser(user, model.getAttribute("password2").toString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userService.save(user);
            model.addAttribute("success", true);
            return "registration";

        } catch (EmailAlreadyExistsException | PasswordsDontMatchException | UserFieldIsEmptyException e) {
            model.addAttribute(e.getMessage().split(" ", 2)[0], e.getMessage());
            return "registration";

        } catch (Exception e) {
            System.err.println("Unknown Exception found while registering user!");
            throw e;
        }
    }


    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute(new AuthenticationRequest());
        return "login";
    }

}

