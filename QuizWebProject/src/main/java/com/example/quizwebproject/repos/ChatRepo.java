package com.example.quizwebproject.repos;

import com.example.quizwebproject.model.users.chat.Chat;
import com.example.quizwebproject.model.users.chat.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepo extends JpaRepository<Chat, Long> {
    Chat findById(long id);

}
