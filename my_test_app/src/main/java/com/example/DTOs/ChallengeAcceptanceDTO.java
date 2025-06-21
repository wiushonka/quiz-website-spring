package com.example.DTOs;

import com.example.model.quizes.Quiz;
import com.example.model.quizes.QuizResult;

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
