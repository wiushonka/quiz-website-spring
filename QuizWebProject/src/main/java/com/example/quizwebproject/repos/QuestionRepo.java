package com.example.quizwebproject.repos;

import com.example.quizwebproject.model.questions.Question;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionRepo extends CrudRepository<Question, Long> {
}