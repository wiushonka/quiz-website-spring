package com.example.quizwebproject;


import com.example.quizwebproject.model.quizes.Quiz;
import com.example.quizwebproject.model.quizes.QuizResult;
import com.example.quizwebproject.model.users.Challenge;
import com.example.quizwebproject.model.users.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ChallengeTest {

    private User sender;
    private User receiver;
    private Quiz quiz;
    private QuizResult quizResult;

    @BeforeEach
    void setUp() {
        sender = mock(User.class);
        receiver = mock(User.class);
        quiz = mock(Quiz.class);
        quizResult = mock(QuizResult.class);
    }

    @Test
    void testChallengeConstructorAndGetters() {
        Challenge challenge = new Challenge(sender, receiver, quiz, quizResult);

        assertNull(challenge.getId(), "ID should be null before persisting");
        assertEquals(sender, challenge.getSender(), "Sender should match the provided sender");
        assertEquals(receiver, challenge.getReceiver(), "Receiver should match the provided receiver");
        assertEquals(quiz, challenge.getQuiz(), "Quiz should match the provided quiz");
        assertEquals(quizResult, challenge.getSenderHS(), "Sender's high score should match the provided quizResult");
    }

    @Test
    void testNoArgsConstructor() {
        Challenge challenge = new Challenge();
        assertNull(challenge.getId());
        assertNull(challenge.getSender());
        assertNull(challenge.getReceiver());
        assertNull(challenge.getQuiz());
        assertNull(challenge.getSenderHS());
    }
}

