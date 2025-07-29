package com.example.quizwebproject.model.users;

import com.example.quizwebproject.model.quizes.Quiz;
import com.example.quizwebproject.model.quizes.QuizResult;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Entity
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private QuizResult senderBestScore;

    @ManyToOne
    private User sender;

    @ManyToOne
    private User receiver;

    @ManyToOne(optional = false)
    private Quiz quiz;

    public Challenge() {}

    public Challenge(@NotNull User sender, User receiver, Quiz quiz, QuizResult senderBestScore) {
        this.sender = sender;
        this.receiver = receiver;
        this.quiz = quiz;
        this.senderBestScore = senderBestScore;
    }

    public Long getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public QuizResult getSenderHS() {
        return senderBestScore;
    }
}
