package com.example.service;

import com.example.model.questions.Question;
import com.example.model.quizes.Quiz;
import com.example.repos.QuestionRepo;
import com.example.repos.QuizRepo;
import jakarta.persistence.Transient;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class QuizCreationService {

    private final QuizRepo quizRepo;

    private final QuestionRepo questionRepo;

    public QuizCreationService(QuizRepo quizRepo, QuestionRepo questionRepo) {
        this.quizRepo = quizRepo;
        this.questionRepo = questionRepo;
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

    public void addOldQuestionToQuiz(Long quizId, Long questionId) {
        Quiz quiz = quizRepo.findById(quizId).orElse(null);
        Question question = questionRepo.findById(questionId).orElse(null);
        if(quiz == null || question == null) {
            throw new IllegalArgumentException("Quiz or Question not found");
        }
        quiz.addQuestion(question);
    }
}
