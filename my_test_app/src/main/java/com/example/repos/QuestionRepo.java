package com.example.repos;

import com.example.model.questions.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface QuestionRepo extends CrudRepository<Question, Long> {
}
