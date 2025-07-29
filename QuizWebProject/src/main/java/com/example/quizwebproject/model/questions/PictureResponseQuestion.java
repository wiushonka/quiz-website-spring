package com.example.quizwebproject.model.questions;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

import java.util.List;

@Entity
public class PictureResponseQuestion extends Question {

    @Lob
    private String imageUrl;

    private String correctAnswer;

    public PictureResponseQuestion() {}

    public PictureResponseQuestion(String question, String correctAnswer, String imageUrl, String category, Double maxPoints) {
        super(question, correctAnswer, category, maxPoints);
        this.correctAnswer = correctAnswer;
        this.imageUrl = imageUrl;
        setRawUserAnswer("");
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
        setRawCorrectAnswers(correctAnswer);
    }

    @Override
    public String getQuestionType() { return "PictureResponseQuestion"; }

    public String getCorrectAnswer() {
        return this.correctAnswer;
    }

    @Override
    public String getQuestion() {
        // You can format the question and image URL for display if needed
        return getRawQuestion();
    }

    @Override
    public void setMaxPoints(Double maxPoints) {
        setRawMaxPoints(maxPoints);
    }

    @Override
    public Double getMaxPoints() {
        return getRawMaxPoints();
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

    @Override
    @Lob
    public List<String> getCorrectAnswers() {
        return List.of(correctAnswer);
    }

    @Override
    public double getResult() {
        String user = getRawUserAnswer();
        if (user == null || user.isBlank()) return 0.0;
        String u = user.trim().replaceAll("\\s+", "").toLowerCase();
        if (u.endsWith(",")) {
            u = u.substring(0, u.length() - 1);
        }
        String c = correctAnswer.trim().replaceAll("\\s+", "").toLowerCase();
        return u.equals(c) ? getMaxPoints() : 0.0;
    }


    @Override
    public String toString() {
        return "PictureResponseQuestion {" +
                "id=" + getId() +
                ", question='" + getQuestion() + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", userAnswer='" + getUserAnswer() + '\'' +
                ", result=" + getResult() +
                ", category='" + getCategory() + '\'' +
                ", maxPoints=" + getMaxPoints() +
                '}';
    }
}
