package com.example.model.questions;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    public Long getId() {
        return id;
    }
    public abstract String getQuestion();
    public abstract void setUserAnswer(String userAnswer);
    public abstract List<String> getCorrectAnswers();
    public abstract double getResult();
    public abstract String getUserAnswer();
    public abstract String getCategory();
    public abstract void setCategory(String category);
}
