package com.example.model.quizes;

import com.example.model.questions.Question;
import com.example.model.questions.TestQuestion;
import com.example.model.users.User;
import com.example.repos.QuizRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class QuizRepositoryTest {

    @Autowired
    private QuizRepo quizRepo;

}
