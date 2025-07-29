package com.example.quizwebproject.repos;

import com.example.quizwebproject.model.users.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Repository
public interface ChallengesRepo extends JpaRepository<Challenge, Long> {

    List<Challenge> findByReceiverId(Long userId);

    @Query("SELECT ch FROM Challenge ch WHERE ch.receiver.id = :userId")
    Page<Challenge> getChals(@Param("userId") Long userId, Pageable pageable);
}
