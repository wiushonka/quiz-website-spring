package com.example.quizwebproject.repos;

import com.example.quizwebproject.model.users.activities.FriendActivity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface FriendActivityRepo extends JpaRepository<FriendActivity, Long> {
    @Query("SELECT fa FROM FriendActivity fa WHERE fa.userId = :userId AND fa.createdAt >= :cutoff ORDER BY fa.createdAt DESC")
    Page<FriendActivity> findByUserId(@Param("userId") Long userId, @Param("cutoff")LocalDateTime cutoff, Pageable pageable);
}
