package com.example.quizwebproject.model.questions;

import jakarta.persistence.Entity;
import jakarta.persistence.Transient;

import java.util.List;

@Entity
public class ResponseQuestion extends Question {

    private String correctAnswer;
    private boolean answerOrdered;

    public ResponseQuestion() {}

    public ResponseQuestion(String question, String correctAnswer, boolean order, String category, Double maxPoints) {
        super(question, correctAnswer, category, maxPoints);
        this.correctAnswer = correctAnswer;
        this.answerOrdered = order;
    }

    public void setQuestion(String question) {
        setRawQuestion(question);
    }

    @Override
    public String getQuestionType() { return "ResponseQuestion"; }

    @Override
    public void setMaxPoints(Double maxPoints) {
        setRawMaxPoints(maxPoints);
    }

    @Override
    public Double getMaxPoints() {
        return getRawMaxPoints();
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
    public String getQuestion() {
        return getRawQuestion();
    }

    @Override
    public void setUserAnswer(String userAnswer) {
        setRawUserAnswer(userAnswer);
    }

    @Transient
    @Override
    public List<String> getCorrectAnswers() {
        return List.of(correctAnswer);
    }

    public boolean isAnswerOrdered() { return answerOrdered; }

    @Override
    public double getResult() {
        String userAnswer = getRawUserAnswer();
        String cleanedUser = userAnswer.replaceAll("\\s+", "");
        String cleanedCorrect = correctAnswer.replaceAll("\\s+", "");

        if (!this.answerOrdered) {
            char[] cU = cleanedUser.toCharArray();
            char[] cC = cleanedCorrect.toCharArray();
            java.util.Arrays.sort(cU);
            java.util.Arrays.sort(cC);
            cleanedUser = new String(cU);
            cleanedCorrect = new String(cC);
        }

        int n = cleanedUser.length();
        int m = cleanedCorrect.length();
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i <= n; i++) dp[i][0] = i;
        for (int j = 0; j <= m; j++) dp[0][j] = j;

        for (int i = 1; i <= n; i++) {
            for (int j = 1; j <= m; j++) {
                if (cleanedUser.charAt(i - 1) == cleanedCorrect.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(
                            dp[i - 1][j - 1],
                            Math.min(dp[i - 1][j], dp[i][j - 1])
                    );
                }
            }
        }

        int dist = dp[n][m];
        return getMaxPoints() * (1.0 - (double) dist / Math.max(n, m));
    }

    @Override
    public String getUserAnswer() {
        return getRawUserAnswer();
    }

    @Override
    public String toString() {
        return "ResponseQuestion {" +
                "id=" + getId() +
                ", question='" + getQuestion() + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", userAnswer='" + getUserAnswer() + '\'' +
                ", result=" + getResult() +
                ", category='" + getCategory() + '\'' +
                ", maxPoints=" + getMaxPoints() +
                ", ordered=" + answerOrdered +
                '}';
    }
}
