package com.example.quizwebproject;

import com.example.quizwebproject.model.questions.FillBlankQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FillBlankQuestionTest {

    private FillBlankQuestion question;

    @BeforeEach
    void setUp() {
        question = new FillBlankQuestion(
                "What is the capital of France?",
                "Geography",
                "Paris",
                10.0
        );
    }

    @Test
    void testGetQuestionType() {
        assertEquals("FillBlankQuestion", question.getQuestionType());
    }

    @Test
    void testGetAndSetMaxPoints() {
        assertEquals(10.0, question.getMaxPoints());

        question.setMaxPoints(15.0);
        assertEquals(15.0, question.getMaxPoints());
    }

    @Test
    void testGetAndSetQuestion() {
        assertEquals("What is the capital of France?", question.getQuestion());

        question.setRawQuestion("New question?");
        assertEquals("New question?", question.getQuestion());
    }

    @Test
    void testGetAndSetUserAnswer() {
        assertEquals("", question.getUserAnswer());

        question.setUserAnswer("Paris");
        assertEquals("Paris", question.getUserAnswer());
    }

    @Test
    void testGetAndSetCategory() {
        assertEquals("Geography", question.getCategory());

        question.setCategory("History");
        assertEquals("History", question.getCategory());
    }

    @Test
    void testGetCorrectAnswers() {
        List<String> correctAnswers = question.getCorrectAnswers();
        assertEquals(1, correctAnswers.size());
        assertEquals("Paris", correctAnswers.get(0));

        question.setRawCorrectAnswers(null);
        assertTrue(question.getCorrectAnswers().isEmpty());
    }

    @Test
    void testGetResult() {
        question.setUserAnswer("Paris");
        assertEquals(100.0, question.getResult());

        question.setUserAnswer("paris");
        assertEquals(100.0, question.getResult());

        question.setUserAnswer("Lyon");
        assertEquals(0.0, question.getResult());

        question.setUserAnswer(null);
        assertEquals(0.0, question.getResult());

        question.setRawCorrectAnswers(null);
        question.setUserAnswer("Paris");
        assertEquals(0.0, question.getResult());
    }

    @Test
    void testToString() {
        question.setUserAnswer("Paris");
        String str = question.toString();
        assertTrue(str.contains("FillBlankQuestion"));
        assertTrue(str.contains("Paris"));
        assertTrue(str.contains("Geography"));
        assertTrue(str.contains("100.0"));
    }
}
