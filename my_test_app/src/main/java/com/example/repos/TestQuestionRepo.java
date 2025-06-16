package com.example.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.model.questions.TestQuestion;

public interface TestQuestionRepo extends JpaRepository<TestQuestion, Long> { }
