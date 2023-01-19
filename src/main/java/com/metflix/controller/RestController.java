package com.metflix.controller;


import com.metflix.auth.AuthenticationRequest;
import com.metflix.auth.AuthenticationResponse;
import com.metflix.auth.AuthenticationService;
import com.metflix.model.Address;
import com.metflix.model.CreditCard;
import com.metflix.model.Movie;
import com.metflix.model.User;
import com.metflix.repositories.AddressRepository;
import com.metflix.repositories.CreditCardRepository;
import com.metflix.repositories.MovieRepository;
import com.metflix.repositories.UserRepository;
import com.metflix.service.TokenService;
import com.sun.security.auth.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import java.util.Map;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api/")
@RequiredArgsConstructor
public class RestController {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;
    private final CreditCardRepository creditCardRepository;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final AuthenticationService authenticationService;



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


    @GetMapping("/authenticate")
    public String getToken(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication);
        System.out.println(tokenService.getJwtFromCookies(request));
        return "Welcome, ";
    }



    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {

        try {
            authenticationService.authenticate(request);
        } catch (Exception e) {
            System.out.println(e);
        }

        return ResponseEntity.ok(authenticationService.authenticate(request));
    }


}
