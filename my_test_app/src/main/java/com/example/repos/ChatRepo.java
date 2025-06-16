package com.example.repos;

import com.example.model.users.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepo extends JpaRepository<Chat, Long> {
    Chat findById(long id);
}
