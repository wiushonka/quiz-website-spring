package com.example.quizwebproject.model.questions;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.List;

@Entity
public class FillBlankQuestion extends Question {

    public FillBlankQuestion() {}

    public FillBlankQuestion(String question, String category, String correctAnswer, Double maxPoints) {
        super(question, correctAnswer, category, maxPoints);
        setRawUserAnswer("");
    }

    @Override
    public String getQuestionType() { return "FillBlankQuestion"; }

    @Override
    public Double getMaxPoints(){
        return getRawMaxPoints();
    }

    @Override
    public void setMaxPoints(Double maxPoints){
        setRawMaxPoints(maxPoints);
    }

    @Override
    public String getQuestion() {
        return getRawQuestion();
    }

    @Override
    public void setUserAnswer(String userAnswer) {
        setRawUserAnswer(userAnswer);
    }

    @Override
    public String getUserAnswer() {
        return getRawUserAnswer();
    }

    @Override
    public String getCategory() {
        return getRawCategory();
    }

    @Override
    public void setCategory(String category) {
        setRawCategory(category);
    }

    @Transient
    @Override
    public List<String> getCorrectAnswers() {
        String correct = getRawCorrectAnswers();
        if (correct == null || correct.isBlank()) return List.of();
        return List.of(correct);
    }

    @Override
    public double getResult() {
        String user = getRawUserAnswer();
        String correct = getRawCorrectAnswers();
        if (user == null || correct == null) return 0.0;

        return user.trim().equalsIgnoreCase(correct.trim()) ? getMaxPoints() : 0.0;
    }

    @Override
    public String toString() {
        return "FillBlankQuestion {" +
                "id=" + getId() +
                ", question='" + getQuestion() + '\'' +
                ", correctAnswer='" + getCorrectAnswers() + '\'' +
                ", userAnswer='" + getUserAnswer() + '\'' +
                ", result=" + getResult() +
                ", category='" + getCategory() + '\'' +
                '}';
    }
}
