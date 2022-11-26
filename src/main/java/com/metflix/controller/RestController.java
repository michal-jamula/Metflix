package com.metflix.controller;


import com.metflix.model.Address;
import com.metflix.model.CreditCard;
import com.metflix.model.Movie;
import com.metflix.model.User;
import com.metflix.repositories.AddressRepository;
import com.metflix.repositories.CreditCardRepository;
import com.metflix.repositories.MovieRepository;
import com.metflix.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/")
public class RestController {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final CreditCardRepository creditCardRepository;


    public RestController(AddressRepository addressRepository, UserRepository userRepository, MovieRepository movieRepository, CreditCardRepository creditCardRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
        this.creditCardRepository = creditCardRepository;
    }



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


}
