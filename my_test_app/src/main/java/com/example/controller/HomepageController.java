package com.example.controller;

import com.example.model.users.FriendRequest;
import com.example.model.users.User;
import com.example.service.FriendService;
import com.example.service.HomepageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomepageController {

    private final HomepageService homepageService;
    private final FriendService friendService;

    public HomepageController(HomepageService homepageService, FriendService friendService) {
        this.homepageService = homepageService;
        this.friendService = friendService;
    }

    @RequestMapping("/homepage")
    public String displayHomepage(Model model, HttpSession session) {
        model.addAttribute("announcements",homepageService.getAnnouncements());
        model.addAttribute("popularQuizs",homepageService.popularQuizs());
        model.addAttribute("getRecentQuizs",homepageService.getRecentQuizs());
        User user = (User) session.getAttribute("user");

        if(user == null) {
            return "login";
        }

        model.addAttribute("user",user);
        model.addAttribute("recentQuizTaking",homepageService.getUserRecentQuizTakes(user.getId()));
        model.addAttribute("recentQuizCreating",homepageService.getRecentQuizCreats(user.getId()));
        model.addAttribute("recentChallenges", homepageService.getRecentChallenges(user.getId()));

        model.addAttribute("acts",homepageService.getRecentFriendActivities(user.getId(),PageRequest.of(0, 30)));

        List<FriendRequest> reqs = friendService.getFriendRequests(user.getId());
        model.addAttribute("friendRequests",reqs);
        List<User> friends = friendService.getFriendsList(user.getId());
        model.addAttribute("friends",friends);

        return "homepage";
    }

    @GetMapping("/createQuiz")
    public String displayCreateQuiz(Model model, HttpSession session) {
        return "quizCreation";
    }
}



