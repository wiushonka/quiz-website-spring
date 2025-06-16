package com.example.repos;

import com.example.model.questions.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionRepo extends CrudRepository<Question, Long> {

}
