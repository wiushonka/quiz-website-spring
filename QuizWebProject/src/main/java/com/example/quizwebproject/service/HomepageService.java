package com.example.quizwebproject.service;

import com.example.quizwebproject.model.quizes.*;
import com.example.quizwebproject.model.users.Challenge;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.model.users.activities.FriendActivity;
import com.example.quizwebproject.model.users.admin.Announcement;
import com.example.quizwebproject.repos.*;
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
    private final ChallengesRepo chalRepo;
    private final QuizResultRepo qrRepo;

    private static final int POPULAR_QUIZ_LIMIT = 10;
    private static final int RECENT_QUIZ_LIMIT = 20;
    private static final int USER_QUIZ_LIMIT = 20;
    private static final int CHALLENGE_LIMIT = 20;
    private static final int FRIEND_ACTIVITY_LIMIT = 20;
    private static final int ANNOUNCEMENT_DAYS = 10;
    private static final int RECENT_DAYS = 1;

    public HomepageService(QuizRepo quizRepo, AnnouncementRepo announcementRepo, UserRepo userRepo,
                           FriendActivityRepo fracRepo, ChallengesRepo chalRepo, QuizResultRepo qrRepo) {
        this.quizRepo = quizRepo;
        this.announcementRepo = announcementRepo;
        this.userRepo = userRepo;
        this.fracRepo = fracRepo;
        this.chalRepo = chalRepo;
        this.qrRepo = qrRepo;
    }

    public List<Announcement> getRecentAnnouncements(Pageable pageable) {
        LocalDateTime lastTenDays = LocalDateTime.now().minusDays(10);
        return this.announcementRepo.getRecentTenDayAnnouncements(lastTenDays,pageable).getContent();
    }

    public List<Quiz> popularQuizs() {
        return this.quizRepo.findByPopularity(PageRequest.of(0, POPULAR_QUIZ_LIMIT)).getContent();
    }

    public List<Quiz> getRecentQuizs() {
        LocalDateTime lastDay = LocalDateTime.now().minusDays(RECENT_DAYS);
        return this.quizRepo.getDayLastTenQuizs(lastDay, PageRequest.of(0, RECENT_QUIZ_LIMIT)).getContent();
    }

    public List<QuizResult> getUserRecentQuizTakes(Long userId) {
        LocalDateTime lastTenDays = LocalDateTime.now().minusDays(ANNOUNCEMENT_DAYS);
        return this.qrRepo.getRecentUserQuizs(lastTenDays, userId, PageRequest.of(0, USER_QUIZ_LIMIT)).getContent();
    }

    public List<Quiz> getRecentQuizCreats(Long userId) {
        LocalDateTime lastTenDays = LocalDateTime.now().minusDays(ANNOUNCEMENT_DAYS);
        return this.quizRepo.getRecentUserQuizCreations(userId, lastTenDays, PageRequest.of(0, USER_QUIZ_LIMIT)).getContent();
    }

    public List<Challenge> getRecentChallenges(Long userId) {
        return this.chalRepo.getChals(userId, PageRequest.of(0, CHALLENGE_LIMIT)).getContent();
    }

    public List<FriendActivity> getRecentFriendActivities(Long userId, Pageable pageable) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        List<User> friends = user.getFriends();
        List<FriendActivity> friendsRecentActs = new ArrayList<>();
        LocalDateTime lastDay = LocalDateTime.now().minusDays(RECENT_DAYS);

        for (User f : friends) {
            List<FriendActivity> recActs = this.fracRepo.findByUserId(f.getId(), lastDay, pageable).getContent();
            if (!recActs.isEmpty()) {
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
