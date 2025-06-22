package com.example.repos;

import com.example.model.quizes.QuizResult;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuizResultRepo extends JpaRepository<QuizResult, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT qr FROM QuizResult qr WHERE qr.user.id = :userId")
    List<QuizResult> getUserHistory(@Param("userId") Long userId);
}
