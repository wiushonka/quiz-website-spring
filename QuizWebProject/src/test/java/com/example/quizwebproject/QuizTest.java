package com.example.quizwebproject;


import com.example.quizwebproject.model.questions.Question;
import com.example.quizwebproject.model.questions.TestQuestion;
import com.example.quizwebproject.model.quizes.Quiz;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.repos.QuizRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class QuizTest {

    @Autowired
    private QuizRepo quizRepo;

    private Quiz quiz;
    private Question q1;
    private Question q2;

    @BeforeEach
    void setUp() {
        String description = "Quiz Test";
        List<Question> questions = new ArrayList<>();
        User author = new User("ika", "cuxo");
        String type = "Difficult";

        List<String> correctAnswers = new ArrayList<>();
        correctAnswers.add("Tbilisi");
        q1 = new TestQuestion(
                "What is the capital of Georgia?",
                correctAnswers,
                "Geography",
                1.0,
                "Mtskheta,Tbilisi,Paris,Rome"
        );

        correctAnswers.remove(0);
        correctAnswers.add("4");
        q2 = new TestQuestion(
                "What is 2+2 equal to?",
                correctAnswers,
                "Maths",
                1.0,
                "1,2,3,4"
        );

        questions.add(q1);
        questions.add(q2);

        quiz = new Quiz(
                false,
                false,
                false,
                false,
                description,
                questions,
                author,
                type
        );
    }

    @Test
    public void testQuizDescription(){
        String newDescription = "Test Quiz";
        assertNotEquals(newDescription, quiz.getDescription());
        assertEquals("Quiz Test", quiz.getDescription());
        quiz.setDescription(newDescription);
        assertEquals(newDescription, quiz.getDescription());
        assertNotEquals("Quiz Test", quiz.getDescription());
    }

    @Test
    public void testQuizRandomize(){
        assertFalse(quiz.isRandomize());
        quiz.setRandomize(true);
        assertTrue(quiz.isRandomize());
    }

    @Test
    public void testQuizQuestions(){
        List<Question> dummy = quiz.getQuestions();
        assertEquals("Tbilisi", dummy.get(0).getCorrectAnswers().get(0));
        assertEquals("Geography", dummy.get(0).getCategory());
        assertEquals("What is the capital of Georgia?", dummy.get(0).getQuestion());
        assertEquals("4", dummy.get(1).getCorrectAnswers().get(0));
        assertEquals("Maths", dummy.get(1).getCategory());
        assertEquals("What is 2+2 equal to?", dummy.get(1).getQuestion());
    }

    @Test
    public void testQuizSetRemoveQuestions(){
        quiz.removeQuestion(q1);
        assertEquals(1, quiz.getQuestions().size());
        quiz.removeIndexQuestion(0);
        assertEquals(0, quiz.getQuestions().size());
        quiz.insertQuestion(q1,0);
        assertEquals(1, quiz.getQuestions().size());
        quiz.insertQuestion(q2,1);
        assertEquals(2, quiz.getQuestions().size());
        quiz.addQuestion(q1);
        assertEquals(3, quiz.getQuestions().size());
    }

    @Test
    public void testBasic(){
        assertEquals("ika", quiz.getAuthor().getUsername());
        assertEquals("cuxo", quiz.getAuthor().getPassword());
        assertEquals("Difficult", quiz.getType());
        quiz.setType("Easy");
        assertEquals("Easy", quiz.getType());
        assertFalse(quiz.isPracticeMode());
        assertFalse(quiz.isMulPages());
        assertFalse(quiz.isQuickResults());
        assertEquals(0, quiz.getHistory().size());
    }

}
