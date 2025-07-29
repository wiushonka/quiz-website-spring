package com.example.quizwebproject;

import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.model.users.chat.Chat;
import com.example.quizwebproject.model.users.chat.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    private User user;
    private Chat chat;
    private Message message;
    private LocalDateTime now;

    @BeforeEach
    public void setUp() {
        user = new User("giorgi", "password");
        chat = new Chat(List.of(user));
        now = LocalDateTime.now();

        message = new Message(now, "Hello world", chat, user);
    }

    @Test
    public void testConstructorSetsFieldsCorrectly() {
        assertEquals(now, message.getDate());
        assertEquals("Hello world", message.getText());
        assertEquals(chat, message.getChat());
        assertEquals(user, message.getUser());
    }

    @Test
    public void testSetChat() {
        Chat newChat = new Chat(List.of(user));
        message.setChat(newChat);
        assertEquals(newChat, message.getChat());
    }

    @Test
    public void testDefaultConstructor() {
        Message emptyMessage = new Message();
        assertNull(emptyMessage.getDate());
        assertNull(emptyMessage.getText());
        assertNull(emptyMessage.getChat());
        assertNull(emptyMessage.getUser());
    }
}
