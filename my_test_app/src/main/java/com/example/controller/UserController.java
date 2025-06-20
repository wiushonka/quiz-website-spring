package com.example.controller;

import com.example.model.users.User;
import com.example.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam("name") String username, @RequestParam("password") String password,
                        HttpSession session) {

        User user = new User(username, password);

        if(userService.isValidPass(user)) {
            session.setAttribute("user", userService.getUser(username));
            return "redirect:/homepage";
        }else{
            return "passwordOrUserIncorrect";
        }
    }

    @GetMapping("/signUp")
    public String showSignUpForm() {
        return "signUp";
    }

    @PostMapping("/signUp")
    public String signUp(@RequestParam("name") String username, @RequestParam("password") String password,
                         HttpSession session) {
        User user = new User(username, password);
        if(userService.userExists(user)) {
            return "userExists";
        }else{
            userService.addNewUser(user);
            return "/login";
        }
    }
}
