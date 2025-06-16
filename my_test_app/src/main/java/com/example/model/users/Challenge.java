package com.example.model.users;

import com.example.model.quizes.Quiz;
import com.example.model.quizes.QuizResult;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Challenge {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private QuizResult senderBestScore;

    @OneToOne
    private User sender;

    @OneToOne
    private User receiver;

    @OneToOne
    private Quiz quiz;

    public Challenge() {}

    public Challenge(User sender, User receiver, Quiz quiz) {
        this.sender = sender;
        this.receiver = receiver;
        this.quiz = quiz;
        this.senderBestScore = sender.getBestScore(quiz);
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
