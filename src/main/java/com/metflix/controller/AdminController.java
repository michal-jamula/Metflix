package com.metflix.controller;


import com.metflix.model.Authority;
import com.metflix.model.Movie;
import com.metflix.model.User;
import com.metflix.repositories.MovieRepository;
import com.metflix.service.AuthorityService;
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

    private final UserService userService;
    private final MovieService movieService;
    private final MovieRepository movieRepository;
    private final AuthorityService authorityService;

    public AdminController(UserService userService, MovieService movieService, MovieRepository movieRepository, AuthorityService authorityService) {
        this.userService = userService;
        this.movieService = movieService;
        this.movieRepository = movieRepository;
        this.authorityService = authorityService;
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
    public String editUserWithId(@RequestParam("id") int userId, Model model) {

        Optional<User> userOptional = userService.findById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Movie with this ID does not exist!");
        }
        return "admin/edit_user";
    }

    @PostMapping("user")
    public String adminUpdateUser (@ModelAttribute("user") User user,
                                   @RequestParam("id") int userId,
                                   Model model) {

        //TODO: remove userId from parameters, make sure that @ModelAttribute returns Optional
        try {
            //User user = userOptional.get();
            userService.updateUser(user);
        } catch (Exception e) {
            return "error";
        }

        return "admin/edit_user";
    }

    /** Requires "id" parameter. Redirects user to /error if the user ID doesn't match anything in the database */
    @PostMapping("movie")
    public String adminMovieEditPost(@ModelAttribute("movie") Optional<Movie> movieOptional,
                                     Model model) {


        try {
            movieService.updateMovie(movieOptional.get());
        } catch (Exception e) {
            return "error";
        }

        return "admin/edit_movie";
    }



    @GetMapping(value = "/authorities/{page-number}")
    public String findPaginatedAuthorities(@PathVariable(name = "page-number") final int pageNo,
                                     @RequestParam(name = "sort-field") final String sortField,
                                     @RequestParam(name = "sort-dir") final String sortDir,
                                     final Model model
    ) {

        final int pageSize = 10;
        final Page<Authority> page = authorityService.findPaginated(pageNo, pageSize, sortField, sortDir);
        final List<Authority> listAuthorities = page.getContent();

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
        // Authorities list
        model.addAttribute("listAuthorities", listAuthorities);
        return "admin/authorities";
    }



    @GetMapping(value = "/userauthorities/{page-number}")
    public String findPaginatedUserAuthorities(@PathVariable(name = "page-number") final int pageNo,
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
        // Authorities list
        model.addAttribute("listUsers", listUsers);
        return "admin/user_authorities";
    }


}