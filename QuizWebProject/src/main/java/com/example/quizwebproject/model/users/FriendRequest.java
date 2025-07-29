package com.example.quizwebproject.model.users;

import jakarta.persistence.*;

@Entity
public class FriendRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User sender;

    @ManyToOne(optional = false)
    private User receiver;

    private boolean result = false;

    public FriendRequest() {}

    public FriendRequest(User sender, User receiver) {
        this.sender = sender;
        this.receiver = receiver;
    }

    public Long getId() { return id; }

    public User getSender() { return sender; }

    public User getReceiver() { return receiver; }

    public boolean getResult() { return result; }

    public void setResult(boolean result) { this.result = result; }
}