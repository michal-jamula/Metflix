package com.metflix.controller;



import com.metflix.exceptions.EmailAlreadyExistsException;
import com.metflix.exceptions.PasswordsDontMatchException;
import com.metflix.exceptions.RegistrationFieldEmptyException;
import com.metflix.model.User;
import com.metflix.repositories.AddressRepository;
import com.metflix.repositories.CreditCardRepository;
import com.metflix.repositories.UserRepository;
import com.metflix.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
public class WebController {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final CreditCardRepository creditCardRepository;
    private final UserService userService;
    private Optional<User> userOptional;
    private Model model;
    private String password2;

    public WebController(final UserRepository userRepository, final AddressRepository addressRepository, final CreditCardRepository creditCardRepository, UserService userService) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.creditCardRepository = creditCardRepository;
        this.userService = userService;
    }


    @GetMapping("/")
    public String index (Model model) {
        return "index";
    }

    @GetMapping("index")
    public String index () {
        return "index";
    }


    @GetMapping("admin")
    public String admin() {
        return "admin_sample";
    }

    @GetMapping("user")
    public String user( Model model) {
        return "user";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("user_main")
    public String user_main() {
        return "user_main";
    }

    @GetMapping("movie_single")
    public String movie_single() {
        return "movie_single";
    }

    //TODO: Check if the given ID exists (user can manually enter id through browser)
    @GetMapping("admin_user_single")
    public String admin_user_single(@RequestParam("id") int userId, Model model) {

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
        } else {
            // Instead of using IF,ELSE, I should go for try catch
        }

        return "admin_user_single";
    }

    @GetMapping("admin_users")
    public String admin_users(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "admin_users";
    }



    @GetMapping("admin_movies")
    public String admin_movies() {
        return "admin_movies";
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


        try {
            List<String> message = userService.validateUser(user, model.getAttribute("password2").toString());
            System.out.println("list received from user service into controller: " + message);
            model.addAttribute(message.get(0), message.get(1));


            if (message.get(0).equals("success")) {
                userService.save(user);
                return "registration";
            }
        }


        catch (Exception e) {
            System.out.println(e);
            System.err.println("Unknown error thrown during user registration, please check asap");
            return "registration";
        }



        return "registration";
    }




}
