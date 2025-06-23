package com.example.service;

import com.example.model.quizes.Quiz;
import com.example.model.quizes.QuizResult;
import com.example.model.users.User;
import com.example.model.users.achievements.Achievements;
import com.example.repos.AchievementsRepo;
import com.example.repos.QuizRepo;
import com.example.repos.QuizResultRepo;
import com.example.repos.UserRepo;
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
        return userRepo.findUserAchievements(userId, Pageable.unpaged());
    }

    public List<Achievements> getRecentAchievements(Long userId) {
        return userRepo.findUserAchievements(userId, PageRequest.of(0, 30));
    }
}
