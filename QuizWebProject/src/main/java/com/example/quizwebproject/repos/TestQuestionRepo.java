package com.example.quizwebproject.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.quizwebproject.model.questions.TestQuestion;
import org.springframework.stereotype.Repository;

@Repository
public interface TestQuestionRepo extends JpaRepository<TestQuestion, Long> { }

