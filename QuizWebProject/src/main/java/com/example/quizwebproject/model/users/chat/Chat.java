package com.example.quizwebproject.model.users.chat;

import com.example.quizwebproject.model.users.User;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    private List<User> users;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Message> messages;

    protected Chat() {}

    public Chat(@NotNull List<User> users) {
        this.users = users;
        for (User user : users) {
            user.getChats().add(this);
        }
        messages = new ArrayList<>();
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

    public List<Message> getMessages() {
        return messages;
    }

    public List<User> getUsers() {
        return users;
    }
}
