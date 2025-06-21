package com.example.repos;

import com.example.model.users.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengesRepo extends JpaRepository<Challenge, Long> {

}
