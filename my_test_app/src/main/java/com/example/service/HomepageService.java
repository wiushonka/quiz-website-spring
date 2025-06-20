package com.example.service;

import com.example.model.quizes.Quiz;
import com.example.model.quizes.QuizResult;
import com.example.model.users.Challenge;
import com.example.model.users.admin.Announcement;
import com.example.repos.AnnouncementRepo;
import com.example.repos.QuizRepo;
import com.example.repos.UserRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HomepageService {
    private final QuizRepo quizRepo;

    private final AnnouncementRepo announcementRepo;

    private final UserRepo userRepo;


    public HomepageService(QuizRepo quizRepo, AnnouncementRepo announcementRepo, UserRepo userRepo) {
        this.quizRepo = quizRepo;
        this.announcementRepo = announcementRepo;
        this.userRepo = userRepo;
    }

    public List<Announcement> getAnnouncements() {
        return this.announcementRepo.findAll();
    }

    public List<Quiz> popularQuizs() {
        return this.quizRepo.findByPopularity(PageRequest.of(0,10)).getContent();
    }

    public List<Quiz> getRecentQuizs() {
        // TODO remove line 43 only tests
        // LocalDateTime secsBefore = LocalDateTime.now().minusSeconds(15);
        LocalDateTime lastDay = LocalDateTime.now().minusDays(1);
        return this.quizRepo.getDayLastTenQuizs(lastDay,PageRequest.of(0,10)).getContent();
    }

    public List<QuizResult> getUserRecentQuizTakes(Long userId) {
        LocalDateTime lastTenDays = LocalDateTime.now().minusDays(10);
        return this.userRepo.getRecentUserQuizs(lastTenDays,PageRequest.of(0,10)).getContent();
    }

    public List<Quiz> getRecentQuizCreats(Long userId) {
        LocalDateTime lastTenDays = LocalDateTime.now().minusDays(10);
        return this.quizRepo.getRecentUserQuizCreations(userId,lastTenDays,PageRequest.of(0,10)).getContent();
    }

    public List<Challenge> getChallenges(Long userId) {
        return this.userRepo.getChallenges(userId,PageRequest.of(0,10));
    }
}
