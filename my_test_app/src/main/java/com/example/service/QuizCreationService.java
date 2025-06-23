package com.example.service;

import com.example.model.questions.Question;
import com.example.model.quizes.Quiz;
import com.example.model.users.User;
import com.example.model.users.achievements.ProlificAuthor;
import com.example.model.users.activities.FriendActivity;
import com.example.model.users.activities.CreatedQuiz;
import com.example.repos.FriendActivityRepo;

import com.example.repos.QuizRepo;
import com.example.repos.UserRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class QuizCreationService {

    private final QuizRepo quizRepo;

    private final FriendActivityRepo fracRepo;

    private final UserRepo userRepo;

    public QuizCreationService(QuizRepo quizRepo, FriendActivityRepo fracRepo, UserRepo userRepo) {
        this.quizRepo = quizRepo;
        this.fracRepo = fracRepo;
        this.userRepo = userRepo;
    }

    public Long createQuiz(Quiz quiz) {
        Quiz saved = quizRepo.save(quiz);
        FriendActivity act = new CreatedQuiz(quiz.getAuthor().getId(),saved.getId(),saved.getAuthor().getUsername());
        User user = userRepo.findById(quiz.getAuthor().getId()).orElseThrow(()->new RuntimeException("User not found"));
        user.getAchievements().add(new ProlificAuthor());
        fracRepo.save(act);
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
