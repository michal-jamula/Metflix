package com.metflix.controller;


import com.metflix.model.*;
import com.metflix.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class RestController {

    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final AuthorityRepository authorityRepository;



    @GetMapping("allusers")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @GetMapping("allmovies")
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("allauthorities")
    public List<Authority> getAllAuthorities() {return authorityRepository.findAll();}

    @GetMapping("getuser/{id}")
    public User getUser(@PathVariable Integer id) {
        return userRepository.findById(id).get();
    }





}
