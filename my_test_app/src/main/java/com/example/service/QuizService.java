package com.example.service;

import com.example.model.questions.Question;
import com.example.model.quizes.Quiz;
import com.example.model.quizes.QuizResult;
import com.example.model.users.User;
import com.example.repos.QuizRepo;
import com.example.repos.UserRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuizService {
    private final QuizRepo quizRepo;

    private final UserRepo userRepo;

    public QuizService(QuizRepo quizRepo, UserRepo userRepo) {
        this.userRepo = userRepo;
        this.quizRepo = quizRepo;
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

        quiz.getHistory().add(result);
        user.getUserHistory().add(result);
    }

    public List<Quiz> getAllQuizzs() {
        return quizRepo.findAll();
    }
}
