package com.metflix.controller;


import com.metflix.model.*;
import com.metflix.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class RestController {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final CreditCardRepository creditCardRepository;
    private final AuthorityRepository authorityRepository;



    @GetMapping("allusers")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @GetMapping("allmovies")
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    @GetMapping("allcards")
    public List<CreditCard> getAllCreditCards() {
        return creditCardRepository.findAll();
    }

    @GetMapping("alladdresses")
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @GetMapping("allauthorities")
    public List<Authority> getAllAuthorities() {return authorityRepository.findAll();}

    @GetMapping("getuser/{id}")
    public User getUser(@PathVariable Integer id) {
        return userRepository.findById(id).get();
    }





}
