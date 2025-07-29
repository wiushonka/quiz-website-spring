package com.example.quizwebproject.controller;

import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.model.users.admin.Announcement;
import com.example.quizwebproject.service.AdminService;
import com.example.quizwebproject.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;

@Controller
public class AdminController {

    public final UserService userService;

    public final AdminService adminService;

    public AdminController(UserService userService, AdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "adminPages/adminPage";
    }

    @GetMapping("/admin/newAnnouncement")
    public String addAnnouncement() {
        return "adminPages/announcementCreator";
    }

    @PostMapping("/admin/newAnnouncement")
    public String addAnnouncement(@RequestParam("title") String title,
                                  @RequestParam("content") String content,
                                  HttpSession session) {
        User author = (User) session.getAttribute("user");
        Announcement announcement = new Announcement(author,title,content);
        adminService.addAnnouncement(announcement);
        return "redirect:/admin";
    }

    @GetMapping("/admin/user")
    public String userManagerPage(Model model,HttpSession session) {
        User admin = (User) session.getAttribute("user");
        List<User> allUsers = userService.getAllUsers();
        allUsers.removeIf(u -> u.getUsername().equals(admin.getUsername()));
        model.addAttribute("users",allUsers);
        return "adminPages/userManagerPage";
    }

    @PostMapping("/admin/removeUser")
    public String removeUser(@RequestParam("userId") Long userId) {
        adminService.deleteUser(userId);
        return "redirect:/admin/user";
    }

    @PostMapping("/admin/promotion")
    public String promoteUser(@RequestParam("userId") Long userId) {
        try {
            adminService.promoteUser(userId);
        } catch (Exception e) {
            return "errorPages/userNotFound";
        }
        return "redirect:/admin/user";
    }

    @GetMapping("/admin/quizManager")
    public String quizManager(Model model) {
        model.addAttribute("quizzs", adminService.getAllQuizzs());
        return "adminPages/quizManagerPage";
    }

    @PostMapping("/admin/clearQuizHistory")
    public String clearQuizHistory(@RequestParam("quizId") Long quizId) {
        adminService.clearQuizHistory(quizId);
        return "redirect:/admin/quizManager";
    }

    @PostMapping("/admin/removeQuiz")
    public String removeQuiz(@RequestParam("quizId") Long quizId) {
        adminService.removeQuiz(quizId);
        return "redirect:/admin/quizManager";
    }

    @GetMapping("/admin/stats")
    public String statsPage(Model model) {
        HashMap<String,Long> stats = adminService.seeStats();
        model.addAttribute("userCount", stats.get("userCount"));
        model.addAttribute("totalQuizsTaken", stats.get("totalQuizsTaken"));
        return "adminPages/statsPage";
    }
}
