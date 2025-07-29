package com.example.quizwebproject.model.questions;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Transient;
import org.antlr.v4.runtime.misc.NotNull;
//import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class TestQuestion extends Question {

    @Lob
    private String savAnswers;

    public TestQuestion() {}

    public TestQuestion(String question, List<String> correctAnswers, String category, Double maxPoints, @NotNull String savAnswers) {
        super(question, String.join(",", correctAnswers), category, maxPoints);
        this.savAnswers = savAnswers.trim();
        setRawUserAnswer(""); // initial blank answer
    }

    @Override
    public String getQuestionType() { return "TestQuestion"; }

    @Override
    public String getCategory() {
        return getRawCategory();
    }

    @Override
    public void setCategory(String category) {
        setRawCategory(category);
    }

    @Override
    public String getQuestion() {
        return getRawQuestion();
    }

    public void setQuestion(String question) {
        setRawQuestion(question);
    }

    @Override
    @Transient
    public List<String> getCorrectAnswers() {
        String raw = getRawCorrectAnswers();
        if (raw == null || raw.isEmpty()) return new ArrayList<>();

        String[] parts = raw.split(",");
        List<String> list = new ArrayList<>();
        for (String p : parts) {
            list.add(p.trim());
        }
        return list;
    }

    public void setCorrectAnswers(List<String> correctAnswers) {
        setRawCorrectAnswers(String.join(",", correctAnswers));
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

    public List<String> getSavAnswers() {
        if (savAnswers == null || savAnswers.isEmpty()) return new ArrayList<>();

        String[] parts = savAnswers.split(",");
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
        String userAns = getRawUserAnswer();
        if (userAns == null || userAns.isEmpty()) {
            return 0.0;
        }

        List<String> correct = getCorrectAnswers();
        List<String> user = new ArrayList<>();
        for (String part : userAns.split(",")) {
            user.add(part.trim());
        }

        int x = 0;
        for (String ans : user) {
            if (correct.contains(ans)) {
                x++;
            }
        }

        x = Math.min(x, correct.size());
        return getMaxPoints() * x / correct.size();
    }

    @Override
    public String toString() {
        return "TestQuestion {" +
                "id=" + getId() +
                ", question='" + getQuestion() + '\'' +
                ", correctAnswers=" + getCorrectAnswers() +
                ", userAnswer='" + getUserAnswer() + '\'' +
                ", savAnswers=" + getSavAnswers() +
                ", result=" + getResult() +
                ", category='" + getCategory() + '\'' +
                ", maxPoints=" + getMaxPoints() +
                '}';
    }
}
