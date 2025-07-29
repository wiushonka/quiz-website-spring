package com.example.quizwebproject.model.questions;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
public class MatchingQuestion extends Question {

    @Lob
    private String leftItems;

    @Lob
    private String rightItems;

    public MatchingQuestion() {}

    public MatchingQuestion(
            String question,
            List<String> leftItems,
            List<String> rightItems,
            String category,
            Double maxPoints) {

        super(
                question,
                String.join(",", rightItems != null ? rightItems : List.of()),
                category,
                maxPoints
        );

        this.leftItems = String.join(",", leftItems != null ? leftItems : List.of());
        this.rightItems = String.join(",", rightItems != null ? rightItems : List.of());
        setRawUserAnswer("");
    }

    @Override
    public String getQuestionType() {
        return "MatchingQuestion";
    }

    public List<String> getLeftItems() {
        return parseList(leftItems);
    }

    public void setLeftItems(List<String> leftItems) {
        this.leftItems = String.join(",", leftItems);
    }

    public List<String> getRightItems() {
        return parseList(rightItems);
    }

    public void setRightItems(List<String> rightItems) {
        this.rightItems = String.join(",", rightItems);
    }

    @Override
    public List<String> getCorrectAnswers() {
        return getRightItems(); // Treat rightItems as correct answers
    }

    public List<String> getUserMatches() {
        return parseList(getRawUserAnswer());
    }

    public void setUserMatches(List<String> matches) {
        setRawUserAnswer(String.join(",", matches));
    }

    @Override
    public double getResult() {
        List<String> correct = getCorrectAnswers();
        List<String> user = getUserMatches();

        if (user.isEmpty() || user.size() != correct.size()) return 0.0;

        int matched = 0;
        for (int i = 0; i < correct.size(); i++) {
            if (correct.get(i).equalsIgnoreCase(user.get(i))) {
                matched++;
            }
        }

        return getMaxPoints() * matched / correct.size();
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

    @Override
    public String toString() {
        return "MatchingQuestion {" +
                "id=" + getId() +
                ", question='" + getQuestion() + '\'' +
                ", leftItems=" + getLeftItems() +
                ", rightItems=" + getRightItems() +
                ", correctAnswers=" + getCorrectAnswers() +
                ", userMatches=" + getUserMatches() +
                ", result=" + getResult() +
                ", category='" + getCategory() + '\'' +
                ", maxPoints=" + getMaxPoints() +
                '}';
    }

    private List<String> parseList(String str) {
        if (str == null || str.isBlank()) return new ArrayList<>();
        return Arrays.stream(str.split(","))
                .map(String::trim)
                .toList();
    }
}
