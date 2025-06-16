package com.example.repos;

import com.example.model.questions.ResponseQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseQuestionRepo extends JpaRepository<ResponseQuestion, Long> {
}
