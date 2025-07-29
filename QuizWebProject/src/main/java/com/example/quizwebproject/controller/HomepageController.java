package com.example.quizwebproject.controller;

import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.service.HomepageService;
import com.example.quizwebproject.service.FriendService;
import com.example.quizwebproject.service.UserPageService;
import com.example.quizwebproject.model.users.FriendRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomepageController {

    private final HomepageService homepageService;
    private final FriendService friendService;
    private final UserPageService userPageService;

    private static final int HOMEPAGE_PAGE_SIZE = 30;

    public HomepageController(HomepageService homepageService, FriendService friendService, UserPageService userPageService) {
        this.homepageService = homepageService;
        this.friendService = friendService;
        this.userPageService = userPageService;
    }

    @RequestMapping("/homepage")
    public String displayHomepage(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null || !homepageService.validUser(user)) {
            return "login";
        }

        try {
            Pageable pageable = PageRequest.of(0, HOMEPAGE_PAGE_SIZE);

            model.addAttribute("announcements", homepageService.getRecentAnnouncements(pageable));
            model.addAttribute("popularQuizs", homepageService.popularQuizs());
            model.addAttribute("getRecentQuizs", homepageService.getRecentQuizs());

            model.addAttribute("user", user);
            model.addAttribute("recentQuizTaking", homepageService.getUserRecentQuizTakes(user.getId()));
            model.addAttribute("recentQuizCreating", homepageService.getRecentQuizCreats(user.getId()));
            model.addAttribute("recentChallenges", homepageService.getRecentChallenges(user.getId()));
            model.addAttribute("acts", homepageService.getRecentFriendActivities(user.getId(), pageable));
            model.addAttribute("userAchis", userPageService.getRecentAchievements(user.getId()));

            List<FriendRequest> reqs = friendService.getFriendRequests(user.getId());
            model.addAttribute("friendRequests", reqs);
            List<User> friends = friendService.getFriendsList(user.getId());
            model.addAttribute("friends", friends);
        } catch (Exception e) {
            return "redirect:/login";
        }

        return "infoPages/homepage";
    }

    @GetMapping("/createQuiz")
    public String displayCreateQuiz() {
        return "quizCreation";
    }

    @GetMapping("/allAnnounces")
    public String displayAllAnnouncements(Model model) {
        model.addAttribute("announcements", homepageService.getRecentAnnouncements(Pageable.unpaged()));
        return "infoPages/allAnnouncements";
    }
}
