package com.example.quizwebproject;


import com.example.quizwebproject.model.questions.Question;
import com.example.quizwebproject.model.questions.TestQuestion;
import com.example.quizwebproject.model.quizes.Quiz;
import com.example.quizwebproject.model.quizes.QuizResult;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.repos.QuizResultRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class QuizResultTest {

    @Autowired
    private QuizResultRepo quizRepo;

    private QuizResult quizResult;

    @BeforeEach
    void setUp() {
        String description = "Quiz Test";
        List<Question> questions = new ArrayList<>();
        User user = new User("ika", "cuxo");
        String type = "Difficult";

        List<String> correctAnswers = new ArrayList<>();
        correctAnswers.add("Tbilisi");
        Question q1 = new TestQuestion(
                "What is the capital of Georgia?",
                correctAnswers,
                "Geography",
                1.0,
                "Mtskheta,Tbilisi,Paris,Rome"
        );

        correctAnswers.remove(0);
        correctAnswers.add("4");
        Question q2 = new TestQuestion(
                "What is 2+2 equal to?",
                correctAnswers,
                "Maths",
                1.0,
                "1,2,3,4"
        );

        questions.add(q1);
        questions.add(q2);

        Quiz quiz = new Quiz(
                false,
                false,
                false,
                false,
                description,
                questions,
                user,
                type
        );
        Long time = 12345L;
        quizResult = new QuizResult(time, 24.0, quiz, user);
    }

    @Test
    public void testQuizResult() {
        assertEquals(12345L, quizResult.getTime());
        assertEquals(24.0, quizResult.getPoints());
        assertEquals("Difficult", quizResult.getQuiz().getType());
        assertEquals("Geography", quizResult.getQuiz().getQuestions().get(0).getCategory());
        assertEquals("ika", quizResult.getUser().getUsername());
        assertEquals("cuxo", quizResult.getUser().getPassword());

        User tmp = new User("cuxo", "ika");
        quizResult.setUser(tmp);
        assertEquals("cuxo", quizResult.getUser().getUsername());
        assertEquals("ika", quizResult.getUser().getPassword());



        String description = "Quiz Test";
        List<Question> questions = new ArrayList<>();
        User user = new User("ika", "cuxo");
        String type = "Difficult";

        List<String> correctAnswers = new ArrayList<>();
        correctAnswers.add("Tbilisi");
        Question q1 = new TestQuestion(
                "What is the capital of Georgia?",
                correctAnswers,
                "Geography",
                1.0,
                "Mtskheta,Tbilisi,Paris,Rome"
        );

        correctAnswers.remove(0);
        correctAnswers.add("4");
        Question q2 = new TestQuestion(
                "What is 2+2 equal to?",
                correctAnswers,
                "Maths",
                1.0,
                "1,2,3,4"
        );

        questions.add(q1);
        questions.add(q2);

        Quiz quiz = new Quiz(
                true, // different from the original
                false,
                false,
                false,
                description,
                questions,
                user,
                type
        );

        assertFalse(quizResult.getQuiz().isRandomize());
        quizResult.setQuiz(quiz);
        assertTrue(quizResult.getQuiz().isRandomize());
    }
}
