package com.metflix.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController {


    @GetMapping("/")
    public String index (Model model) {

        model.addAttribute("hello, Hello World!");
        return "index";
    }

    @GetMapping("index")
    public String index () {
        return "index";
    }

    @GetMapping("registration")
    public String registration() {
        return "registration";
    }

    @GetMapping("admin")
    public String admin() {
        return "admin_sample";
    }

    @GetMapping("user")
    public String user() {
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

    @GetMapping("admin_user_single")
    public String admin_user_single() {
        return "admin_user_single";
    }

    @GetMapping("admin_users")
    public String admin_users() {
        return "admin_users";
    }

    @GetMapping("admin_movies")
    public String admin_movies() {
        return "admin_movies";
    }





}
