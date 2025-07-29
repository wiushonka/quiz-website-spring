package com.example.quizwebproject.repos;

import com.example.quizwebproject.model.users.chat.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessagesRepo extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE m.chat.id = :chatId ORDER BY m.date ASC")
    Page<Message> findTopByChatIdOrderByDateAsc(@Param("chatId") Long chatId, Pageable pageable);
}
