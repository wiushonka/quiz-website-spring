package com.example.service;

import com.example.model.questions.Question;
import com.example.model.quizes.Quiz;
import com.example.repos.QuestionRepo;
import com.example.repos.QuizRepo;
import jakarta.persistence.Transient;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class QuizCreationService {

    private final QuizRepo quizRepo;

    public QuizCreationService(QuizRepo quizRepo, QuestionRepo questionRepo) {
        this.quizRepo = quizRepo;
    }

    public Long createQuiz(Quiz quiz) {
        Quiz saved = quizRepo.save(quiz);
        return saved.getId();
    }

    public void addNewQuestionToQuiz(Long quizId, Question question) {
        Quiz quiz = quizRepo.findById(quizId).orElse(null);
        if(quiz == null) {
            throw new IllegalArgumentException("Quiz not found");
        }
        quiz.addQuestion(question);
    }
}
