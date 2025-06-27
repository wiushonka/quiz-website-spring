package com.example.model.questions;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Transient;

import java.util.List;

@Entity
public class ResponseQuestion extends Question{

    private String question;

    private String correctAnswer;

    private String userAnswer;

    private boolean answerOrdered;

    private String category;

    private int maxPoints;

    public ResponseQuestion() {}

    public ResponseQuestion(String question, String correctAnswer, boolean order, String category, int maxPoints) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.userAnswer = "";
        this.answerOrdered = order;
        this.category = category;
        this.maxPoints = maxPoints;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public void setCategory(String category) { this.category = category; }

    @Override
    public int getMaxPoints() {
        return this.maxPoints;
    }

    @Override
    public String getQuestion() {
        return this.question;
    }

    @Override
    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    @Transient
    @Override
    public List<String> getCorrectAnswers() {
        return List.of(this.correctAnswer);
    }

    @Override
    public double getResult() {
        String cleanedUser = userAnswer.replaceAll(" ", "");
        String cleanedCorrect = correctAnswer.replaceAll(" ", "");

        if(!this.answerOrdered) {
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
        return 100 * (1.0 - (double) dist / Math.max(n, m));
    }

    @Override
    public String getUserAnswer() {
        return this.userAnswer;
    }

    public boolean isAnswerOrdered() {
        return this.answerOrdered;
    }

    @Override
    public String toString() {
        return "ResponseQuestion {" +
                "id=" + getId() +
                ", question='" + question + '\'' +
                ", correctAnswer='" + correctAnswer + '\'' +
                ", userAnswer='" + userAnswer + '\'' +
                ", result=" + getResult() + '\'' +
                ", cat: " + this.category + '}';
    }
}
