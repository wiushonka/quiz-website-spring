package com.example.quizwebproject.controller;


import com.example.quizwebproject.model.quizes.Quiz;
import com.example.quizwebproject.model.quizes.QuizResult;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.service.UserPageService;
import com.example.quizwebproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class UserPageController {

    private final UserPageService upService;

    private final UserService userService;

    public UserPageController(UserService userService, UserPageService upservice) {
        this.userService = userService;
        this.upService = upservice;
    }

    @GetMapping("/user/{userId}")
    public String userPage(@PathVariable Long userId, Model model, HttpSession session,
                           @RequestParam(value = "newFriendRequestSent", required = false) Boolean sent) {
        User user = userService.getUserById(userId);
        if(user == null) throw new RuntimeException("user not found");
        model.addAttribute("user", user);
        model.addAttribute("achis",upService.userAchievements(userId));
        User sessionUser = (User) session.getAttribute("user");
        model.addAttribute("sessionUser", sessionUser);
        model.addAttribute("newFriendRequestSent",sent);
        return "userPage";
    }

    @GetMapping("/user/{userId}/quizs")
    public String getUserQuizs(@PathVariable Long userId, Model model) {
        List<Quiz> userQuizs = upService.getUserQuizs(userId);
        model.addAttribute("userQuizs", userQuizs);
        return "infoPages/userQuizs";
    }

    @GetMapping("/user/{userId}/history")
    public String getUser(@PathVariable Long userId, Model model) {
        List<QuizResult> hist = upService.getUserHistory(userId);
        model.addAttribute("hist", hist);
        return "userHistory";
    }
}

