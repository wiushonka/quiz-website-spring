package com.example.service;

import com.example.model.quizes.Quiz;
import com.example.model.quizes.QuizResult;
import com.example.model.users.Challenge;
import com.example.model.users.User;
import com.example.model.users.activities.FriendActivity;
import com.example.model.users.admin.Announcement;
import com.example.repos.AnnouncementRepo;
import com.example.repos.FriendActivityRepo;
import com.example.repos.QuizRepo;
import com.example.repos.UserRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HomepageService {
    private final QuizRepo quizRepo;

    private final AnnouncementRepo announcementRepo;

    private final UserRepo userRepo;

    private final FriendActivityRepo fracRepo;

    public HomepageService(QuizRepo quizRepo, AnnouncementRepo announcementRepo, UserRepo userRepo,
                           FriendActivityRepo fracRepo) {
        this.quizRepo = quizRepo;
        this.announcementRepo = announcementRepo;
        this.userRepo = userRepo;
        this.fracRepo = fracRepo;
    }

    public List<Announcement> getRecentAnnouncements(Pageable pageable) {
        LocalDateTime lastTenDays = LocalDateTime.now().minusDays(10);
        return this.announcementRepo.getRecentTenDayAnnouncements(lastTenDays,pageable).getContent();
    }

    public List<Quiz> popularQuizs() {
        return this.quizRepo.findByPopularity(PageRequest.of(0,10)).getContent();
    }

    public List<Quiz> getRecentQuizs() {
        // TODO remove line 43 only tests
        // LocalDateTime secsBefore = LocalDateTime.now().minusSeconds(15);
        LocalDateTime lastDay = LocalDateTime.now().minusDays(1);
        return this.quizRepo.getDayLastTenQuizs(lastDay,PageRequest.of(0,20)).getContent();
    }

    public List<QuizResult> getUserRecentQuizTakes(Long userId) {
        LocalDateTime lastTenDays = LocalDateTime.now().minusDays(10);
        return this.userRepo.getRecentUserQuizs(lastTenDays,userId,PageRequest.of(0,20)).getContent();
    }

    public List<Quiz> getRecentQuizCreats(Long userId) {
        LocalDateTime lastTenDays = LocalDateTime.now().minusDays(10);
        return this.quizRepo.getRecentUserQuizCreations(userId,lastTenDays,PageRequest.of(0,20)).getContent();
    }

    public List<Challenge> getRecentChallenges(Long userId) {
        return this.userRepo.getRecentChallenges(userId,PageRequest.of(0,20));
    }

    public List<FriendActivity> getRecentFriendActivities(Long userId, Pageable pageable) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<User> friends = user.getFriends();
        List<FriendActivity> friendsRecentActs = new ArrayList<>();
        LocalDateTime lastDay = LocalDateTime.now().minusDays(1);
        for(User f : friends) {
            List<FriendActivity> recActs = this.fracRepo.findByUserId(f.getId(),lastDay,pageable).getContent();
            if(!recActs.isEmpty()) {
                friendsRecentActs.addAll(recActs);
            }
        }

        return friendsRecentActs;
    }

    public boolean validUser(User user) {
        List<User> allUser = userRepo.findAll();
        return !allUser.contains(user);
    }
}
