package com.example.model.questions;

import com.example.repos.TestQuestionRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class TestQuestionRepositoryTest {

    @Autowired
    private TestQuestionRepo repository;


}
