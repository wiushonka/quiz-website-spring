package com.example.controller;

import com.example.model.users.FriendRequest;
import com.example.model.users.User;
import com.example.service.FriendService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }

    @PostMapping("/friend/send")
    public String sendFriendRequest(@RequestParam("receiverId") Long receiverId,
                                    HttpSession session,
                                    RedirectAttributes redData) {
        User sen = (User)session.getAttribute("user");

        if (sen == null) {
            return "redirect:/login";
        }

        FriendRequest req = friendService.sendFriendRequest(sen.getId(), receiverId);
        if (req == null) {
            redData.addAttribute("newFriendRequestSent", false);
        }else {
            redData.addAttribute("newFriendRequestSent", true);
        }
        return "redirect:/user/" + receiverId;
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
        friendService.endFriendship(remover.getId(),friendId);
        return "redirect:/homepage";
    }

    @GetMapping("/searchFriends")
    public String searchFriends(HttpSession session,
                                Model model) {
        User user = (User)session.getAttribute("user");
        List<User> nonFriends = friendService.getNonFriendUsers(user.getId());
        model.addAttribute("nonFriends", nonFriends);
        return "searchFriends";
    }
}
