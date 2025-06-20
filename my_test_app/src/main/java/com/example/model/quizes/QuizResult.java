package com.example.model.quizes;

import com.example.model.users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class QuizResult {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long time;
    private double points;

    @ManyToOne()
    private User user;

    @ManyToOne()
    private Quiz quiz;

    private LocalDateTime resultDate;

    public QuizResult() {}

    public QuizResult(Long time, double points, Quiz quiz, User user) {
        this.time = time;
        this.points = points;
        this.quiz = quiz;
        this.user = user;
    }

    @PrePersist
    protected void onCreate() {
        resultDate = LocalDateTime.now();
    }

    public LocalDateTime getResultDate() {
        return resultDate;
    }

    public Long getId() {
        return id;
    }

    public Long getTime() {
        return time;
    }

    public double getPoints() {
        return points;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
