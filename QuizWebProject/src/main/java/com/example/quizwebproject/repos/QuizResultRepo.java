package com.example.quizwebproject.repos;

import com.example.quizwebproject.model.quizes.QuizResult;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuizResultRepo extends JpaRepository<QuizResult, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT qr FROM QuizResult qr WHERE qr.user.id = :userId")
    List<QuizResult> getUserHistory(@Param("userId") Long userId);

    @Query("SELECT qr FROM QuizResult qr WHERE qr.quiz.id=:quizId AND qr.user.id=:userId")
    List<QuizResult> pastPerformance(@Param("userId")Long userId, @Param("quizId")Long quizId);

    @Query("SELECT qr FROM QuizResult qr WHERE qr.quiz.id=:quizId ORDER BY qr.points DESC,qr.time ASC,qr.resultDate DESC")
    List<QuizResult> getQuizLeaderboard(@Param("quizId")Long quizId);

    @Query("SELECT qr FROM QuizResult qr WHERE qr.quiz.id=:quizId AND qr.resultDate>=:cutoff ORDER BY qr.points DESC,qr.time ASC,qr.resultDate DESC")
    List<QuizResult> getLastDayLb(@Param("quizId")Long quizId,@Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT qr FROM QuizResult qr WHERE qr.quiz.id=:quizId AND qr.resultDate>=:cutoff")
    List<QuizResult> getRecentHistory(@Param("quizId")Long quizId,@Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT qr FROM QuizResult qr WHERE qr.quiz.id=:quizId")
    List<QuizResult> getByQuizId(@Param("quizId")Long quizId);

    @Query("SELECT qr FROM QuizResult qr WHERE qr.resultDate > :cutoff AND qr.user.id = :userId")
    Page<QuizResult> getRecentUserQuizs(@Param("cutoff") LocalDateTime cutoff, @Param("userId") Long userId,
                                        Pageable pageable);

}
