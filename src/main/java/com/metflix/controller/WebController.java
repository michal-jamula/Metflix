package com.metflix.controller;


import com.metflix.model.Address;
import com.metflix.model.CreditCard;
import com.metflix.model.Movie;
import com.metflix.model.User;
import com.metflix.repositories.CreditCardRepository;
import com.metflix.repositories.MovieRepository;
import com.metflix.repositories.UserRepository;
import com.metflix.service.AddressService;
import com.metflix.service.CreditCardService;
import com.metflix.service.MovieService;
import com.metflix.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

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


    /** Requires "id" parameter. Redirects user to /error if the user ID doesn't match anything in the database */
    @GetMapping("admin_user_single")
    public String admin_user_single(@RequestParam("id") int userId, Model model) {

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User with this ID does not exist!");
        }
        return "admin_user_single";
    }

    /*
        Utilises pagination and sorting, pageSize is hard-coded to 10 atm
     */
    @GetMapping(value = "/admin_users/{page-number}")
    public String findPaginatedUsers(@PathVariable(name = "page-number") final int pageNo,
                                    @RequestParam(name = "sort-field") final String sortField,
                                    @RequestParam(name = "sort-dir") final String sortDir,
                                    final Model model
                                    ) {

        final int pageSize = 10;
        final Page<User> page = userService.findPaginated(pageNo, pageSize, sortField, sortDir);
        final List<User> listUsers = page.getContent();

        // In ideal cases the response should be encapsulated in a class.
        // That's to keep pagination & sorting separate from other variables
        // pagination parameters
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        // sorting parameters
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        // Users list
        model.addAttribute("listUsers", listUsers);
        return "admin_users";
    }




    @GetMapping(value = "/admin_movies/{page-number}")
    public String findPaginatedMovies(@PathVariable(name = "page-number") final int pageNo,
                                    @RequestParam(name = "sort-field") final String sortField,
                                    @RequestParam(name = "sort-dir") final String sortDir,
                                    final Model model) {

        final int pageSize = 10;
        final Page<Movie> page = movieService.findPaginated(pageNo, pageSize, sortField, sortDir);
        final List<Movie> listMovies = page.getContent();

        // In ideal cases the response should be encapsulated in a class.
        // That's to keep pagination & sorting separate from other variables
        // pagination parameters
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        // sorting parameters
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        // Users list
        model.addAttribute("listMovies", listMovies);
        return "admin_movies";
    }


    @GetMapping("registration")
    public String registration(Model model, User user) {
        model.addAttribute("user", new User());
        return "registration";
    }


    @GetMapping(value = "/admin_cc/{page-number}")
    public String findPaginatedCreditCards(@PathVariable(name = "page-number") final int pageNo,
                                           @RequestParam(name = "sort-field") final String sortField,
                                           @RequestParam(name = "sort-dir") final String sortDir,
                                           final Model model) {

        final int pageSize = 10;
        final Page<CreditCard> page = creditCardService.findPaginated(pageNo, pageSize, sortField, sortDir);
        final List<CreditCard> listCards = page.getContent();

        // In ideal cases the response should be encapsulated in a class.
        // That's to keep pagination & sorting separate from other variables
        // pagination parameters
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        // sorting parameters
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        // Users list
        model.addAttribute("listCards", listCards);
        return "admin_credit_cards";
    }



    @GetMapping(value = "/admin_addr/{page-number}")
    public String findPaginatedAddresses(@PathVariable(name = "page-number") final int pageNo,
                                           @RequestParam(name = "sort-field") final String sortField,
                                           @RequestParam(name = "sort-dir") final String sortDir,
                                           final Model model) {

        final int pageSize = 10;
        final Page<Address> page = addressService.findPaginated(pageNo, pageSize, sortField, sortDir);
        final List<Address> listAddresses = page.getContent();

        // In ideal cases the response should be encapsulated in a class.
        // That's to keep pagination & sorting separate from other variables
        // pagination parameters
        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalItems", page.getTotalElements());
        // sorting parameters
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        // Users list
        model.addAttribute("listAddresses", listAddresses);
        return "admin_addresses";
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


    //TODO: Fix the functionality of saving a user
    @PostMapping("admin_user_single")
    public String adminUpdateUser (@ModelAttribute("user") Optional<User> userOptional,
                                    @RequestParam("id") int userId,
                                    Model model) {

        if (userOptional.isEmpty()) {
            return "error";
        }
        User user = userOptional.get();

        userService.updateUserWithId(user, user.getId());
        System.out.println("WebController: User updated successfully");

        return "admin_user_single";
    }


}