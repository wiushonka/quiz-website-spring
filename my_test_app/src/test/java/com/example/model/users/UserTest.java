package com.example.model.users;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.model.quizes.Quiz;
import com.example.model.quizes.QuizResult;
import com.example.model.users.chat.Chat;
import com.example.repos.ChatRepo;
import com.example.repos.QuizRepo;
import com.example.repos.UserRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
@Transactional
public class UserTest {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private ChatRepo chatRepo;

    @Autowired
    private QuizRepo quizRepo;


    @Test
    public void test() {
    }
}
