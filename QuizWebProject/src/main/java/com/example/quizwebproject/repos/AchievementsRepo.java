package com.example.quizwebproject.repos;

import com.example.quizwebproject.model.users.achievements.Achievements;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AchievementsRepo extends JpaRepository<Achievements, Long> {
    @Query("SELECT a FROM Achievements a WHERE a.user.id = :userId ORDER BY a.dateTime DESC")
    List<Achievements> findUserAchievements(Long userId, Pageable pageable);
}
