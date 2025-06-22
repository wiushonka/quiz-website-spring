package com.example.model.users.achievements;

import com.example.model.users.User;
import jakarta.persistence.Entity;

@Entity
public class AmateurAuthor extends Achievements {

    public AmateurAuthor(User user) {
        super(user);
    }

    public AmateurAuthor() {}

    @Override
    public String getName() {
        String name = "Amateur Author—The user created a quiz.";
        return name;
    }
}
