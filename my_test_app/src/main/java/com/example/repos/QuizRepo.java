package com.example.repos;

import com.example.model.questions.Question;
import com.example.model.quizes.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepo extends JpaRepository<Quiz, Long> {
    List<Quiz> findByType(String type);
    Quiz getIdByDescription(String description);
}
