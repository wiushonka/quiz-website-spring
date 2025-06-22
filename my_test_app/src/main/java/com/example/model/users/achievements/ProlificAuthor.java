package com.example.model.users.achievements;

import com.example.model.users.User;
import jakarta.persistence.Entity;

@Entity
public class ProlificAuthor extends Achievements {

    public ProlificAuthor(User user) {
        super(user);
    }

    public ProlificAuthor() {}

    @Override
    public String getName() {
        String name = "Prolific Author—The user created five quizzes.";
        return name;
    }
}
