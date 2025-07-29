package com.example.quizwebproject.service;

import com.example.quizwebproject.DTOs.statsDTO;
import com.example.quizwebproject.model.questions.Question;
import com.example.quizwebproject.model.quizes.Quiz;
import com.example.quizwebproject.model.quizes.QuizResult;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.model.users.achievements.Achievements;
import com.example.quizwebproject.model.users.achievements.IAmTheGreatest;
import com.example.quizwebproject.model.users.activities.FriendActivity;
import com.example.quizwebproject.model.users.activities.QuizTaken;
import com.example.quizwebproject.repos.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

@Service
@Transactional
public class QuizService {
    private final QuizRepo quizRepo;
    private final UserRepo userRepo;
    private final FriendActivityRepo fracRepo;
    private final QuizResultRepo quizResultRepo;
    private final AchievementsRepo achievementsRepo;

    public QuizService(QuizRepo quizRepo, UserRepo userRepo, FriendActivityRepo fracRepo,
                       QuizResultRepo quizResultRepo, AchievementsRepo achievementsRepo) {
        this.userRepo = userRepo;
        this.quizRepo = quizRepo;
        this.fracRepo = fracRepo;
        this.quizResultRepo = quizResultRepo;
        this.achievementsRepo = achievementsRepo;
    }

    public Quiz getQuizById(Long id) {
        return quizRepo.findById(id).orElse(null);
    }

    public double calculateScore(@NotNull Quiz quiz) {
        List<Question> questions = quiz.getQuestions();
        double score = 0;
        for(Question q: questions) {
            score += q.getResult();
        }
        return score;
    }

    public void addNewResult(long elapsedTime, double score, Long quizId, Long userId) {
        Quiz quiz = quizRepo.findById(quizId).orElse(null);
        User user = userRepo.findById(userId).orElse(null);
        if(quiz == null || user == null) throw new IllegalArgumentException("Quiz or User not found");

        QuizResult result = new QuizResult(elapsedTime, score, quiz, user);

        quizResultRepo.save(result);

        quiz.getHistory().add(result);
        user.getUserHistory().add(result);

        FriendActivity act = new QuizTaken(user.getId(),result.getId(),user.getUsername());
        User us = userRepo.findById(quiz.getAuthor().getId()).orElseThrow(()->new RuntimeException("User not found"));

        fracRepo.save(act);

        List<QuizResult> allResults = quizResultRepo.getByQuizId(quizId);
        QuizResult bestScore = allResults.stream()
                .sorted(Comparator
                        .comparing(QuizResult::getPoints, Comparator.reverseOrder()) // highest points first
                        .thenComparing(QuizResult::getTime)                           // shortest time
                        .thenComparing(QuizResult::getResultDate, Comparator.reverseOrder()) // most recent
                )
                .findFirst()
                .orElse(null);


        if(bestScore != null && bestScore.getId().equals(result.getId())) {
            Achievements ach = new IAmTheGreatest(user);
            achievementsRepo.save(ach);
            user.getAchievements().add(ach);
        }

    }

    public List<Quiz> getAllQuizzs() {
        return quizRepo.findAll();
    }

    public List<QuizResult> getUserPastPerformance(Long userId, Long quizId) {
        return quizResultRepo.pastPerformance(userId,quizId);
    }

    public List<QuizResult> getAllTimeLeaderboard(Long quizId) {
        return quizResultRepo.getQuizLeaderboard(quizId);
    }

    public List<QuizResult> getLastDayLb(Long quizId) {
        LocalDateTime lastDay = LocalDateTime.now().minusDays(1);
        return quizResultRepo.getLastDayLb(quizId,lastDay);
    }

    public List<QuizResult> getRecentHistory(Long quizId) {
        LocalDateTime lastDay = LocalDateTime.now().minusDays(1);
        return quizResultRepo.getRecentHistory(quizId,lastDay);
    }

    public statsDTO getStats(Long quizId) {
        Quiz quiz = quizRepo.findById(quizId).orElseThrow(() -> new RuntimeException("Quiz not found"));
        List<QuizResult> hist = quizResultRepo.getByQuizId(quizId);
        if (hist.isEmpty()) {
            return new statsDTO(0.0, 0.0, 0.0, 0L, 0);
        }
        long totalTime = 0L;
        double totalScore = 0D;
        double maxScore = Double.MIN_VALUE;
        double minScore = Double.MAX_VALUE;
        HashSet<User> users = new HashSet<>();

        for (QuizResult qr : hist) {
            users.add(qr.getUser());
            totalTime += qr.getTime();
            totalScore += qr.getPoints();
            maxScore = Math.max(maxScore, qr.getPoints());
            minScore = Math.min(minScore, qr.getPoints());
        }
        int userCount = users.size();
        double avgScore = totalScore / userCount;
        long avgTime = totalTime / userCount;
        return new statsDTO(avgScore, maxScore, minScore, avgTime, userCount);
    }

    public String trimBrackets(String input) {
        if (input != null && input.startsWith("[") && input.endsWith("]") && input.length() > 1) {
            return input.substring(1, input.length() - 1).trim();
        }
        return input;
    }
}
