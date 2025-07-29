package com.example.quizwebproject.repos;

import com.example.quizwebproject.model.quizes.QuizResult;
import com.example.quizwebproject.model.users.Challenge;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.model.users.achievements.Achievements;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.id = :userId")
    Optional<User> findWithLock(@Param("userId") Long userId);

    @Query("SELECT a FROM User u JOIN u.achis a WHERE u.id = :userId")
    Page<Achievements> findUserAchievements(@Param("userId") Long userId, Pageable pageable);
}
