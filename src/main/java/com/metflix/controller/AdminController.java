package com.metflix.controller;


import com.metflix.model.Address;
import com.metflix.model.CreditCard;
import com.metflix.model.Movie;
import com.metflix.model.User;
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
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/")
public class AdminController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final AddressService addressService;
    private final MovieService movieService;
    private final CreditCardService creditCardService;
    private final MovieRepository movieRepository;


    public AdminController(UserRepository userRepository, UserService userService, AddressService addressService, MovieService movieService, CreditCardService creditCardService, MovieRepository movieRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.addressService = addressService;
        this.movieService = movieService;
        this.creditCardService = creditCardService;
        this.movieRepository = movieRepository;
    }



    // TODO: These can probably be combined to make the code easier to read

    @GetMapping(value = "/address/{page-number}")
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
        return "admin/addresses";
    }





    @GetMapping(value = "/cc/{page-number}")
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
        return "admin/credit_cards";
    }




    @GetMapping(value = "/movies/{page-number}")
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
        return "admin/movies";
    }

    /** Requires "id" parameter. Redirects user to /error if the user ID doesn't match anything in the database */
    @GetMapping("movie")
    public String adminMovieEditGet(@RequestParam("id") int movieId, Model model) {

        Optional<Movie> movieOptional = movieRepository.findById(movieId);

        if (movieOptional.isPresent()) {
            Movie movie = movieOptional.get();
            model.addAttribute("movie", movie);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie with this ID does not exist!");
        }
        return "admin/edit_movie";
    }


    /*
    Utilises pagination and sorting, pageSize is hard-coded to 10 atm
 */
    @GetMapping(value = "/users/{page-number}")
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
        return "admin/users";
    }



    /** Requires "id" parameter. Redirects user to /error if the user ID doesn't match anything in the database */
    @GetMapping("user")
    public String adminEditUserGet(@RequestParam("id") int userId, Model model) {

        Optional<User> userOptional = userRepository.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie with this ID does not exist!");
        }
        return "admin/edit_user";
    }

    @PostMapping("user")
    public String adminUpdateUser (@ModelAttribute("user") Optional<User> userOptional,
                                   @RequestParam("id") int userId,
                                   Model model) {
        if (userOptional.isEmpty()) {
            return "error";
        }
        User user = userOptional.get();

        userService.updateUserWithId(user, user.getId());
        System.out.println("AdminController: User successfully updated by admin");

        return "admin/edit_user";
    }

    /** Requires "id" parameter. Redirects user to /error if the user ID doesn't match anything in the database */
    @PostMapping("movie")
    public String adminMovieEditPost(@ModelAttribute("movie") Optional<Movie> movieOptional,
                                     @RequestParam("id") int movieId,
                                     Model model) {

        if (movieOptional.isEmpty()) {
            throw new HttpServerErrorException(HttpStatus.BAD_REQUEST, "Movie with this ID does not exist");
        }

        Movie movie = movieOptional.get();

        movieService.updateMovieWithId(movie, movie.getId());
        System.out.println("AdminController: Movie successfully updated by admin");


        return "admin/edit_movie";
    }



}