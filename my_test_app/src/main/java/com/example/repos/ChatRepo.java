package com.example.repos;

import com.example.model.users.chat.Chat;
import com.example.model.users.chat.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepo extends JpaRepository<Chat, Long> {
    Chat findById(long id);

    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId ORDER BY m.date DESC")
    List<Message> findTopByChatIdOrderByDateDesc(@Param("chatId") Long chatId, Pageable pageable);
}
