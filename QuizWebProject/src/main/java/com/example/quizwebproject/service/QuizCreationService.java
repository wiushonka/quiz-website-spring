package com.example.quizwebproject.service;

import com.example.quizwebproject.model.questions.Question;
import com.example.quizwebproject.model.quizes.Quiz;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.model.users.achievements.ProlificAuthor;
import com.example.quizwebproject.model.users.activities.FriendActivity;
import com.example.quizwebproject.model.users.activities.QuizCreation;
import com.example.quizwebproject.repos.FriendActivityRepo;

import com.example.quizwebproject.repos.QuestionRepo;
import com.example.quizwebproject.repos.QuizRepo;
import com.example.quizwebproject.repos.UserRepo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@Transactional
public class QuizCreationService {

    private final QuizRepo quizRepo;

    private final FriendActivityRepo fracRepo;

    private final UserRepo userRepo;

    private final QuestionRepo questionRepo;

    public QuizCreationService(QuizRepo quizRepo, FriendActivityRepo fracRepo, UserRepo userRepo, QuestionRepo questionRepo, QuizService quizService) {
        this.quizRepo = quizRepo;
        this.fracRepo = fracRepo;
        this.userRepo = userRepo;
        this.questionRepo = questionRepo;
    }

    public Long createQuiz(Quiz quiz) {
        Quiz saved = quizRepo.save(quiz);
        FriendActivity act = new QuizCreation(quiz.getAuthor().getId(),saved.getId(),saved.getAuthor().getUsername());
        User user = userRepo.findById(quiz.getAuthor().getId()).orElseThrow(()->new RuntimeException("User not found"));
        user.getAchievements().add(new ProlificAuthor());
        fracRepo.save(act);
        return saved.getId();
    }

    public void addNewQuestionToQuiz(Long quizId, Question question) {
        Quiz quiz = quizRepo.findById(quizId).orElseThrow(()->new RuntimeException("Quiz not found"));
        quiz.addQuestion(question);
    }

    public void editQuestionInQuiz(Long quizId, int index, Question question) {
        Quiz quiz = quizRepo.findById(quizId).orElseThrow(()->new RuntimeException("Quiz not found"));
        Question oldQ= quiz.getQuestions().get(index);
        quiz.getQuestions().set(index, question);
        questionRepo.delete(oldQ);
    }

    public void removeQuestion(Long quizId, int index) {
        Quiz quiz = quizRepo.findById(quizId).orElseThrow(()->new RuntimeException("Quiz not found"));
        Question oldQ= quiz.getQuestions().get(index);
        quiz.getQuestions().remove(index);
    }
}
