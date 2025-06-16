package com.example.model.questions;

import com.example.repos.ResponseQuestionRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ResponseQuestionTest {
    @Autowired
    private ResponseQuestionRepo repository;

    private void printAllQuestions(String header) {
        System.out.println("\n=== " + header + " ===");
        List<ResponseQuestion> all = repository.findAll();
        for (ResponseQuestion q : all) {
            System.out.println(q.toString());
        }
        System.out.println("====================\n");
    }

    @Test
    public void testSaveAndFind() {
        ResponseQuestion question = new ResponseQuestion("What is the color of sky?", "blue", true, "colors",1);
        question.setUserAnswer("blue");

        // Save entity
        ResponseQuestion saved = repository.save(question);

        // Retrieve from DB
        ResponseQuestion found = repository.findById(saved.getId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getQuestion()).isEqualTo("What is the color of sky?");
        assertThat(found.getCorrectAnswers()).containsExactly("blue");
        assertThat(found.getUserAnswer()).isEqualTo("blue");
    }

    @Test
    public void testResultOrderSensitive() {
        ResponseQuestion question = new ResponseQuestion("Spell 'abc'", "abc", true,"anbani",1);
        question.setUserAnswer("abc");
        repository.save(question);
        assertThat(question.getResult()).isEqualTo(100.0);

        question.setUserAnswer("bac"); // wrong order
        assertThat(question.getResult()).isLessThan(100.0);
    }

    @Test
    public void testResultOrderInsensitive() {
        ResponseQuestion question = new ResponseQuestion("Spell 'abc'", "abc", false,"banbani",1);
        question.setUserAnswer("bac");
        repository.save(question);
        assertThat(question.getResult()).isEqualTo(100.0); // order ignored, so full score

        question.setUserAnswer("abcd"); // extra character, less than 100%
        double result = question.getResult();
        assertThat(result).isLessThan(100.0).isGreaterThan(0.0);
    }

    @Test
    public void testUpdateQuestion() {
        ResponseQuestion q = new ResponseQuestion("Initial?", "answer", true,"gingango",1);
        q.setUserAnswer("answer");

        ResponseQuestion saved = repository.save(q);

        saved.setQuestion("Updated?");
        saved.setUserAnswer("updated answer");
        repository.save(saved);

        ResponseQuestion updated = repository.findById(saved.getId()).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getQuestion()).isEqualTo("Updated?");
        assertThat(updated.getUserAnswer()).isEqualTo("updated answer");
    }

    @Test
    public void testDeleteQuestion() {
        ResponseQuestion q = new ResponseQuestion("To be deleted?", "yes", true,"lingangu",1);
        q.setUserAnswer("yes");

        ResponseQuestion saved = repository.save(q);
        Long id = saved.getId();

        repository.delete(saved);

        boolean exists = repository.findById(id).isPresent();
        assertThat(exists).isFalse();
    }

    @Test
    public void testPrintAllInDb() {
        printAllQuestions("Current DB contents");
        assertTrue(true);
    }

    @Test
    public void testAddMultipleQuestionsAndPrint() {
        ResponseQuestion q1 = new ResponseQuestion("Capital of France?", "Paris", true,"banbani",1);
        q1.setUserAnswer("Paris");

        ResponseQuestion q2 = new ResponseQuestion("2 + 2?", "4", true,"banbani",1);
        q2.setUserAnswer("4");

        ResponseQuestion q3 = new ResponseQuestion("Colors in German flag?", "black red gold", false,"banbani",1);
        q3.setUserAnswer("black gold red");

        repository.save(q1);
        repository.save(q2);
        repository.save(q3);

        printAllQuestions("After Adding 3 Questions");

        List<ResponseQuestion> allQuestions = repository.findAll();
        assertThat(allQuestions).hasSizeGreaterThanOrEqualTo(3);
    }
}