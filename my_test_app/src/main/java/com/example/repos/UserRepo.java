package com.example.repos;

import com.example.model.quizes.QuizResult;
import com.example.model.users.Challenge;
import com.example.model.users.User;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Query("SELECT qr FROM QuizResult qr WHERE qr.resultDate > :cutoff")
    Page<QuizResult> getRecentUserQuizs(@Param("cutoff") LocalDateTime cutoff, Pageable pageable);

    @Query("SELECT chals FROM Challenge chals WHERE chals.receiver.id = :userId")
    List<Challenge> getChallenges(@Param("userId")Long userId, PageRequest pageable);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.id = :userId")
    Optional<User> findWithLock(@Param("userId") Long userId);
}
