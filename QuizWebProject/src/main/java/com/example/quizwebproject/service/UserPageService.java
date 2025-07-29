package com.example.quizwebproject.service;

import com.example.quizwebproject.model.quizes.Quiz;
import com.example.quizwebproject.model.quizes.QuizResult;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.model.users.achievements.Achievements;
import com.example.quizwebproject.repos.AchievementsRepo;
import com.example.quizwebproject.repos.QuizRepo;
import com.example.quizwebproject.repos.QuizResultRepo;
import com.example.quizwebproject.repos.UserRepo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserPageService {
    private final UserRepo userRepo;

    private final QuizRepo quizRepo;

    private final QuizResultRepo quizResultRepo;

    public UserPageService(UserRepo userRepo, QuizRepo quizRepo, QuizResultRepo quizResultRepo) {
        this.quizRepo = quizRepo;
        this.userRepo = userRepo;
        this.quizResultRepo = quizResultRepo;
    }

    public List<QuizResult> getUserHistory(Long userId) {
        return quizResultRepo.getUserHistory(userId);
    }

    public List<Quiz> getUserQuizs(Long userId) {
        return quizRepo.findQuizByAuthorId(userId);
    }

    public List<Achievements> userAchievements(Long userId) {
        return userRepo.findUserAchievements(userId, Pageable.unpaged()).getContent();
    }

    public List<Achievements> getRecentAchievements(Long userId) {
        return userRepo.findUserAchievements(userId, PageRequest.of(0, 30)).getContent();
    }
}

