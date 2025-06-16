package com.example.service;

import com.example.model.questions.Question;
import com.example.model.quizes.Quiz;
import com.example.repos.QuizRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class QuizService {
    private final QuizRepo quizRepo;

    public QuizService(QuizRepo quizRepo) {
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

}
