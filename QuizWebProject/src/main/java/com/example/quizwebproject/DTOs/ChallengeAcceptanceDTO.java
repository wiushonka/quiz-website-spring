package com.example.quizwebproject.DTOs;

import com.example.quizwebproject.model.quizes.Quiz;
import com.example.quizwebproject.model.quizes.QuizResult;

public class ChallengeAcceptanceDTO {

    private final Quiz quiz;

    private final QuizResult quizResult;

    public ChallengeAcceptanceDTO(Quiz quiz, QuizResult quizResult) {
        this.quiz = quiz;
        this.quizResult = quizResult;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public QuizResult quizResult() {
        return quizResult;
    }
}
