package com.metflix.controller;


import com.metflix.model.Movie;
import com.metflix.model.User;
import com.metflix.model.Enums.MovieTypeEnum;
import com.metflix.repositories.MovieRepository;
import com.metflix.service.MovieService;
import com.metflix.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/user/")
public class UserController {

    private final MovieRepository movieRepository;
    private final MovieService movieService;
    private final UserService userService;

    public UserController(MovieRepository movieRepository, MovieService movieService, UserService userService) {
        this.movieRepository = movieRepository;
        this.movieService = movieService;
        this.userService = userService;
    }

    @GetMapping("movies")
    public String user_main(Model model) {


        List<String> movieTypeList = movieRepository.findDistinctByType();

        for (String movieType : movieTypeList) {
            model.addAttribute(movieType, movieRepository.findMoviesByType(MovieTypeEnum.valueOf(movieType)));
        }
        model.addAttribute("listOfMovieTypes", movieTypeList);


        return "user/main_movies";
    }

    @GetMapping("account")
    public String userAccount( Model model) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        refreshUserAuthorities(auth);

        User user = new User();
        if (auth != null && auth.getPrincipal() instanceof User) {
            user = (User) auth.getPrincipal();
        }
        model.addAttribute("success", true);
        model.addAttribute("user", user);

        return "user/account";
    }


    @GetMapping("movie/{id}")
    public String movie_single(@PathVariable("id")int id,
                               Model model) {

        //TODO: The controller is directly accessing the repo !?!?!
        Optional<Movie> movieOptional = movieRepository.findById(id);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();


        if (movieOptional.isEmpty()) {
            System.err.println("user tried to view a movie which ID did not exist");
            return "error";
        }
        Movie movie = movieOptional.get();

        refreshUserAuthorities(auth);

        //Show modal if user is unsubscribed
        User user = new User();
        if (auth != null && auth.getPrincipal() instanceof User) {
            user = (User) auth.getPrincipal();
        }

        model.addAttribute("movie", movie);
        model.addAttribute("userAuthorities", user.getAuthorities());

        return "user/movie_single";
    }

    private void refreshUserAuthorities(Authentication auth){
        try {
            var user = (User)auth.getPrincipal();
            userService.refreshUserSessionDetails(user.getId());

        } catch (Exception e) {
            System.out.println("Unknown Error while trying to update user's authorities");
            e.printStackTrace();
        }

    }

}