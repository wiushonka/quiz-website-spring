package com.example.quizwebproject;

import com.example.quizwebproject.model.questions.MatchingQuestion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchingQuestionTest {

    private MatchingQuestion question;

    @BeforeEach
    void setUp() {
        question = new MatchingQuestion(
                "Match countries with capitals",
                List.of("France", "Germany", "Italy"),
                List.of("Paris", "Berlin", "Rome"),
                "Geography",
                15.0
        );
    }

    @Test
    void testGetQuestionType() {
        assertEquals("MatchingQuestion", question.getQuestionType());
    }

    @Test
    void testGetLeftAndRightItems() {
        assertEquals(List.of("France", "Germany", "Italy"), question.getLeftItems());
        assertEquals(List.of("Paris", "Berlin", "Rome"), question.getRightItems());

        question.setLeftItems(List.of("A", "B"));
        question.setRightItems(List.of("1", "2"));

        assertEquals(List.of("A", "B"), question.getLeftItems());
        assertEquals(List.of("1", "2"), question.getRightItems());
    }

    @Test
    void testGetCorrectAnswers() {
        assertEquals(List.of("Paris", "Berlin", "Rome"), question.getCorrectAnswers());
    }

    @Test
    void testUserMatches() {
        assertTrue(question.getUserMatches().isEmpty());

        question.setUserMatches(List.of("Paris", "Berlin", "Rome"));
        assertEquals(List.of("Paris", "Berlin", "Rome"), question.getUserMatches());
    }

    @Test
    void testMaxPoints() {
        assertEquals(15.0, question.getMaxPoints());

        question.setMaxPoints(20.0);
        assertEquals(20.0, question.getMaxPoints());
    }

    @Test
    void testGetAndSetQuestionAndCategory() {
        assertEquals("Match countries with capitals", question.getQuestion());
        question.setRawQuestion("New question?");
        assertEquals("New question?", question.getQuestion());

        assertEquals("Geography", question.getCategory());
        question.setCategory("History");
        assertEquals("History", question.getCategory());
    }

    @Test
    void testUserAnswerDelegation() {
        question.setUserAnswer("Paris,Berlin,Rome");
        assertEquals("Paris,Berlin,Rome", question.getUserAnswer());
    }

    @Test
    void testToString() {
        question.setUserMatches(List.of("Paris", "Berlin", "Rome"));
        question.setMaxPoints(15.0);

        String str = question.toString();

        assertTrue(str.contains("MatchingQuestion"));
        assertTrue(str.contains("France"));
        assertTrue(str.contains("Paris"));
        assertTrue(str.contains("Paris"));
        assertTrue(str.contains("result=100.0"));
        assertTrue(str.contains("maxPoints=15.0"));
    }
}
