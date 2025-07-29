package com.example.quizwebproject.repos;

import com.example.quizwebproject.model.quizes.Quiz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuizRepo extends JpaRepository<Quiz, Long> {

    @Query("SELECT q FROM Quiz q ORDER BY SIZE(q.history) DESC")
    Page<Quiz> findByPopularity(Pageable pageable);

    @Query("SELECT q FROM Quiz q WHERE q.creationDate >= :cutoff ORDER BY q.creationDate DESC")
    Page<Quiz> getDayLastTenQuizs(@Param("cutoff") LocalDateTime cutoff, Pageable pageable);

    @Query("SELECT q FROM Quiz q WHERE q.creationDate >= :cutoff AND q.author.id = :userId")
    Page<Quiz> getRecentUserQuizCreations(@Param("userId") Long userId,
                                          @Param("cutoff") LocalDateTime cutoff, Pageable pageable);

    List<Quiz> findQuizByAuthorId(Long authorId);
}
