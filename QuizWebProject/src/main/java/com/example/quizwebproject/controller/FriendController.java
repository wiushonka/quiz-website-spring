package com.example.quizwebproject.controller;

import com.example.quizwebproject.model.users.FriendRequest;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.model.users.activities.FriendActivity;
import com.example.quizwebproject.service.FriendService;
import com.example.quizwebproject.service.HomepageService;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;

@Controller
public class FriendController {
    private final FriendService friendService;
    private final HomepageService homepageService;

    public FriendController(FriendService friendService, HomepageService homepageService) {
        this.friendService = friendService;
        this.homepageService = homepageService;
    }

    @PostMapping("/friend/send")
    public String sendFriendRequest(@RequestParam("receiverId") Long receiverId, HttpSession session, RedirectAttributes redData) {
        User sen = (User)session.getAttribute("user");

        if (sen==null) {
            return "redirect:/login";
        }
        try {
            FriendRequest req = friendService.sendFriendRequest(sen.getId(), receiverId);
            redData.addFlashAttribute("newFriendRequestSent", req != null);
        } catch (Exception e) {
            redData.addFlashAttribute("newFriendRequestSent", false);
        }
        return "redirect:/user/"+receiverId;
    }

    @PostMapping("friend/accept")
    public String acceptFriendRequest(@RequestParam("requestId") Long reqId) {
        friendService.acceptFriendRequest(reqId);
        return "redirect:/homepage";
    }

    @PostMapping("friend/reject")
    public String rejectFriendRequest(@RequestParam("requestId") Long reqId) {
        friendService.rejectFriendRequest(reqId);
        return "redirect:/homepage";
    }

    @PostMapping("/removeFriend")
    public String removeFriend(@RequestParam("friendId") Long friendId, HttpSession session) {
        User remover = (User) session.getAttribute("user");
        friendService.endFriendship(remover.getId(), friendId);
        return "redirect:/homepage";
    }

    @GetMapping("/searchFriends")
    public String searchFriends(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        try {
            List<User> nonFriends = friendService.getNonFriendUsers(user.getId());
            model.addAttribute("nonFriends", nonFriends);
        } catch (Exception e) {
            model.addAttribute("nonFriends", Collections.emptyList());
        }
        return  "friendStuff/searchFriends";
    }

    @GetMapping("/showAllFriendActs")
    public String showAllFriendActs(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        try {
            List<FriendActivity> acts = homepageService.getRecentFriendActivities(user.getId(), Pageable.unpaged());
            model.addAttribute("acts", acts);
        } catch (Exception e) {
            model.addAttribute("acts", Collections.emptyList());
        }
        return "friendStuff/showAllFriendActs";
    }
}
