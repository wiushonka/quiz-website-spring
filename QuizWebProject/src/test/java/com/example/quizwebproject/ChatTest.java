package com.example.quizwebproject;

import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.model.users.chat.Chat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ChatTest {

    private User user1;
    private User user2;
    private Chat chat;

    @BeforeEach
    public void setUp() {
        user1 = new User("giorgi1", "pass1");
        user2 = new User("giorgi2", "pass2");

        chat = new Chat(new ArrayList<>(List.of(user1, user2)));
    }

    @Test
    public void testConstructorAddsChatToUsers() {
        assertTrue(user1.getChats().contains(chat));
        assertTrue(user2.getChats().contains(chat));
        assertEquals(2, chat.getUsers().size());
    }

    @Test
    public void testAddUser() {
        User user3 = new User("giorgi3", "pass3");

        chat.addUser(user3);

        assertTrue(chat.getUsers().contains(user3));
        assertTrue(user3.getChats().contains(chat));
    }

    @Test
    public void testKickUser() {
        chat.kickUser(user1);

        assertFalse(chat.getUsers().contains(user1));
        assertFalse(user1.getChats().contains(chat));
    }

    @Test
    public void testGetMessagesInitiallyEmpty() {
        assertNotNull(chat.getMessages());
        assertTrue(chat.getMessages().isEmpty());
    }
}
