package com.example.quizwebproject.model.users.achievements;

import com.example.quizwebproject.model.users.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Achievements {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private User user;

    private LocalDateTime dateTime;

    public Achievements() {}

    public Achievements(User user) {
        this.user = user;
    }

    @PrePersist
    protected void onCreate() {
        this.dateTime = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public abstract String getName();
}
