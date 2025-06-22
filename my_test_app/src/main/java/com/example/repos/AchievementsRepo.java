package com.example.repos;

import com.example.model.users.achievements.Achievements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AchievementsRepo extends JpaRepository<Achievements, Long> {
}
