package com.example.model.quizes;

import com.example.model.users.User;
import jakarta.persistence.*;

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

    public QuizResult() {}

    public QuizResult(Long time, double points, Quiz quiz, User user) {
        this.time = time;
        this.points = points;
        this.quiz = quiz;
        this.user = user;
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
