package com.metflix.controller;


import com.metflix.auth.AuthenticationRequest;
import com.metflix.auth.AuthenticationResponse;
import com.metflix.auth.AuthenticationService;
import com.metflix.model.LoginRequest;
import com.metflix.model.User;
import com.metflix.repositories.MovieRepository;
import com.metflix.repositories.UserRepository;
import com.metflix.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.buf.Utf8Decoder;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.net.HttpCookie;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class WebController {

    private final UserRepository userRepository;
    private final UserService userService;
    private final MovieRepository movieRepository;
    private final MovieService movieService;
    private final CreditCardService creditCardService;
    private final AddressService addressService;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final AuthenticationService authenticationService;


    @GetMapping("/")
    public String index (Model model) {
        return "index";
    }

    @GetMapping("index")
    public String index () {
        return "index";
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
    public String registerUser(@ModelAttribute("user")Optional<User> userOptional, Model model, @ModelAttribute("password2")String password2) {

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




    @GetMapping("login")
    public String login(Model model) {
//        model.addAttribute("username", "");
//        model.addAttribute("password", "");
        model.addAttribute("authenticationRequest", new AuthenticationRequest());
        return "login";
    }


    @PostMapping(value = "/login" , consumes =  MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE
//            ,produces = {
//            MediaType.APPLICATION_ATOM_XML_VALUE,
//            MediaType.APPLICATION_JSON_VALUE}
    )
    public String login (@RequestBody MultiValueMap<String, String> authenticationRequest, final HttpServletResponse response) {
        AuthenticationRequest request = new AuthenticationRequest(authenticationRequest.get("email").get(0), authenticationRequest.get("password").get(0));

        Cookie cookie = new Cookie("Authorization", authenticationService.authenticate(request).getToken());
        response.addCookie(cookie);

        return "user/account";

    }



//    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
//    public String postToken(@ModelAttribute("loginRequest") LoginRequest loginData, HttpServletResponse response) {
//
//        String username = loginData.getUsername();
//        String password = loginData.getPassword();
//
//        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//
//
//        if(authentication.isAuthenticated()) {
//
//            String token = tokenService.generateToken(authentication);
//
//
//
//            //create a cookie with a JWT token
//            Cookie cookie = new Cookie("Authorization", token);
//            cookie.setHttpOnly(true);
//            cookie.setMaxAge(3600);
//
//
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Authorization", cookie.getValue());
//
//
//            response.addCookie(cookie);
//
//            System.out.println(response.getHeaderNames());
//            System.out.println(token);
//            System.out.println(response.getHeader("Set-Cookie"));
//
//            return "user/account";
//        } else {
//            return "login";
//        }
//
//    }



}

