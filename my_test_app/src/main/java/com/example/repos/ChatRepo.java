package com.example.repos;

import com.example.model.users.chat.Chat;
import com.example.model.users.chat.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatRepo extends JpaRepository<Chat, Long> {
    Chat findById(long id);

    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId ORDER BY m.date ASC")
    Page<Message> findTopByChatIdOrderByDateAsc(@Param("chatId") Long chatId, Pageable pageable);
}
