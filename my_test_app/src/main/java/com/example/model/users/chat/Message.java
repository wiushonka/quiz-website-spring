package com.example.model.users.chat;

import com.example.model.users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime date;

    @Lob
    private String text;

    @ManyToOne
    private Chat chat;

    @ManyToOne
    private User user;

    public Message() {}

    public Message(LocalDateTime date, String text, Chat chat, User user) {
        this.date = date;
        this.text = text;
        this.chat = chat;
        this.user = user;
    }

    public Long getId() { return id; }

    public LocalDateTime getDate() { return date; }

    public String getText() { return text; }

    public Chat getChat() { return chat; }

    public void setChat(Chat chat) { this.chat = chat; }

    public User getUser() { return user; }
}
