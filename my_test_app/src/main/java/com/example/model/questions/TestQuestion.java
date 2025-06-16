package com.example.model.questions;

import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class TestQuestion extends Question {

    private String question;

    @Lob
    private String correctAnswers;

    private String userAnswer;

    private String category;

    @Lob
    private String savAnswers;

    private int maxPoints;

    public TestQuestion() {}

    public TestQuestion(String question, List<String> correctAnswers, String category, int maxPoints, @NotNull String savAnswers) {
        this.question = question;
        setCorrectAnswers(correctAnswers);
        this.savAnswers = savAnswers.trim();
        this.category = category;
        this.maxPoints = maxPoints;
        this.userAnswer = "";
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
    public String getQuestion() {
        return this.question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<String> getCorrectAnswers() {
        if (this.correctAnswers == null || this.correctAnswers.isEmpty()) {
            return new ArrayList<>();
        }
        String[] parts = this.correctAnswers.split(",");
        List<String> list = new ArrayList<>();
        for (String p : parts) {
            list.add(p.trim());
        }
        return list;
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        this.correctAnswers = String.join(",", correctAnswers);
    }

    @Override
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    @Override
    public String getUserAnswer() {
        return this.userAnswer;
    }

    public List<String> getSavAnswers() {
        if (this.savAnswers == null || this.savAnswers.isEmpty()) {
            return new ArrayList<>();
        }
        String[] parts = this.savAnswers.split(",");
        List<String> list = new ArrayList<>();
        for (String p : parts) {
            list.add(p.trim());
        }
        return list;
    }

    public void setSavAnswers(List<String> savAnswers) {
        this.savAnswers = String.join(",", savAnswers);
    }

    @Override
    public double getResult() {
        if (this.userAnswer == null || this.userAnswer.isEmpty()) {
            return 0.0;
        }

        List<String> correct = getCorrectAnswers();
        List<String> user = new ArrayList<>();
        for (String part : userAnswer.split(",")) {
            user.add(part.trim());
        }

        int x = 0;
        for (String ans : user) {
            if (correct.contains(ans)) {
                x++;
            }
        }

        x = Math.min(x, correct.size());
        return 100.0 * x / correct.size();
    }

    @Override
    public String toString() {
        return "TestQuestion {" +
                "id=" + getId() +
                ", question='" + question + '\'' +
                ", correctAnswers=" + getCorrectAnswers() +
                ", userAnswer='" + userAnswer + '\'' +
                ", savAnswers=" + getSavAnswers() +
                ", result=" + getResult() + '\'' +
                ", cat: " + this.category + '}';
    }
}
