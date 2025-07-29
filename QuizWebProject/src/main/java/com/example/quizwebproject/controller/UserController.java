package com.example.quizwebproject.controller;

import com.example.quizwebproject.model.quizes.Quiz;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.service.QuizService;
import com.example.quizwebproject.service.UserPageService;
import com.example.quizwebproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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
            User realUser = userService.getUser(username);
            session.setAttribute("user", realUser);
            userService.checkAchivements(realUser.getId());
            return "redirect:/homepage";
        }else{
            return "errorPages/passwordOrUserIncorrect";
        }
    }

    @GetMapping("/signUp")
    public String showSignUpForm() {
        return "signUp";
    }

    @PostMapping("/signUp")
    public String signUp(@RequestParam("name") String username, @RequestParam("password") String password) {
        User user = new User(username, password);
        if(userService.userExists(user)) {
            return "errorPages/userExists";
        }else{
            userService.addNewUser(user);
            return "login";
        }
    }
}

