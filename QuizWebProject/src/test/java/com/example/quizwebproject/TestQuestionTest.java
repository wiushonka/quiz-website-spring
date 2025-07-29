package com.example.quizwebproject;

import com.example.quizwebproject.model.questions.TestQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestQuestionTest {

    private TestQuestion question;

    @BeforeEach
    void setUp() {
        question = new TestQuestion(
                "Select all prime numbers",
                List.of("2", "3", "5", "7"),
                "Math",
                25.0,
                "2,3,5,7"
        );
    }

    @Test
    void testGetQuestionType() {
        assertEquals("TestQuestion", question.getQuestionType());
    }

    @Test
    void testGetAndSetCategory() {
        assertEquals("Math", question.getCategory());
        question.setCategory("Number Theory");
        assertEquals("Number Theory", question.getCategory());
    }

    @Test
    void testGetAndSetQuestion() {
        assertEquals("Select all prime numbers", question.getQuestion());
        question.setQuestion("New question?");
        assertEquals("New question?", question.getQuestion());
    }

    @Test
    void testGetAndSetCorrectAnswers() {
        assertEquals(List.of("2", "3", "5", "7"), question.getCorrectAnswers());

        question.setCorrectAnswers(List.of("11", "13"));
        assertEquals(List.of("11", "13"), question.getCorrectAnswers());
    }

    @Test
    void testGetAndSetSavAnswers() {
        assertEquals(List.of("2", "3", "5", "7"), question.getSavAnswers());

        question.setSavAnswers(List.of("11", "13"));
        assertEquals(List.of("11", "13"), question.getSavAnswers());
    }

    @Test
    void testGetAndSetMaxPoints() {
        assertEquals(25.0, question.getMaxPoints());
        question.setMaxPoints(30.0);
        assertEquals(30.0, question.getMaxPoints());
    }

    @Test
    void testGetAndSetUserAnswer() {
        assertEquals("", question.getUserAnswer());
        question.setUserAnswer("2,3");
        assertEquals("2,3", question.getUserAnswer());
    }

    @Test
    void testGetResult() {
        // No answer
        question.setUserAnswer("");
        assertEquals(0.0, question.getResult());

        // Partial correct answers
        question.setUserAnswer("2, 3");
        assertEquals(50.0, question.getResult());

        // All correct answers
        question.setUserAnswer("2,3,5,7");
        assertEquals(100.0, question.getResult());

        // More answers than correct (should cap at 100%)
        question.setUserAnswer("2,3,5,7,11");
        assertEquals(100.0, question.getResult());

        // Some wrong answers mixed in (only count correct)
        question.setUserAnswer("2,4,6");
        assertEquals(25.0, question.getResult());
    }

    @Test
    void testToString() {
        question.setUserAnswer("2,3,5");
        question.setMaxPoints(25.0);

        String str = question.toString();

        assertTrue(str.contains("TestQuestion"));
        assertTrue(str.contains("Select all prime numbers"));
        assertTrue(str.contains("2"));
        assertTrue(str.contains("3"));
        assertTrue(str.contains("5"));
        assertTrue(str.contains("result="));
        assertTrue(str.contains("maxPoints=25.0"));
    }
}
