package com.example.model.questions;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.List;

// TODO : es qliavoba gasasworebelia tavidan bolomde xelaxlaa dasaweri

@Entity
public class FillBlankQuestion extends Question {

    private String question;

    private String correctAnswer;

    private String userAnswer;

    private String category;

    private int maxPoints;

    public FillBlankQuestion() {}

    public FillBlankQuestion(String question, String category, String correctAnswer, int maxPoints) {
        this.question = question;
        this.category = category;
        this.correctAnswer = correctAnswer;
        this.userAnswer = "";
    }

    @Override
    public String getQuestion() {
        return question;
    }

    @Override
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    @Transient
    @Override
    public List<String> getCorrectAnswers() {
        return List.of();
    }

    @Override
    public double getResult() {
        if (userAnswer == null) return 0;
        String ans = userAnswer.toLowerCase();
        String realAns = correctAnswer.toLowerCase();
        if(ans.equals(realAns)) return 1.0;
        return 0;
    }

    @Override
    public String getUserAnswer() {
        if (userAnswer == null) return "";
        return userAnswer;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public int getMaxPoints() {
        return maxPoints;
    }
}
