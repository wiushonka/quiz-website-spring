package com.example.quizwebproject.DTOs;

public class statsDTO {

    public Double avgScore;

    public Double maxScore;

    public Double minScore;

    public Long avgTime;

    public int userCnt;

    public statsDTO(Double avgScore, Double maxScore, Double minScore, Long avgTime, int userCnt) {
        this.avgScore = avgScore;
        this.maxScore = maxScore;
        this.minScore = minScore;
        this.avgTime = avgTime;
        this.userCnt = userCnt;
    }

    public Double getAvgScore() {
        return avgScore;
    }

    public Double getMaxScore() {
        return maxScore;
    }

    public Double getMinScore() {
        return minScore;
    }

    public Long getAvgTime() {
        return avgTime;
    }

    public int getUserCnt() {
        return userCnt;
    }
}
