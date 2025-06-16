package com.example.model.users;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<User> users = new ArrayList<>();

    @ElementCollection
    private List<String> messages = new ArrayList<>();

    public Chat() {
        this.users = new ArrayList<>();
        this.messages = new ArrayList<>();
    }

    public Chat(List<User> users) {
        this.users = users;
        for (User user : users) {
            user.getChats().add(this);
        }
    }

    public Long getId() { return id; }

    public void addUser(User user) {
        users.add(user);
        user.getChats().add(this);
    }

    public void kickUser(User user) {
        users.remove(user);
        user.getChats().remove(this);
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }

    public List<User> getUsers() {
        return users;
    }
}

