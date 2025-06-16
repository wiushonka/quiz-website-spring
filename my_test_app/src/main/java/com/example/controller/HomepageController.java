package com.example.controller;

import com.example.model.users.User;
import com.example.service.HomepageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomepageController {

    private final HomepageService homepageService;

    public HomepageController(HomepageService homepageService) {
        this.homepageService = homepageService;
    }

    @RequestMapping("/homepage")
    public String displayHomepage(Model model) {
        model.addAttribute("announcements",homepageService.getAnnouncements());
        model.addAttribute("popularQuizs",homepageService.popularQuizs());
        return "homepage";
    }

    @GetMapping("/createQuiz")
    public String displayCreateQuiz(Model model, HttpSession session) {
        return "quizCreation";
    }
}



