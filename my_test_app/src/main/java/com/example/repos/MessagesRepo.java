package com.example.repos;

import com.example.model.users.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessagesRepo extends JpaRepository<Message, Long> {
}
