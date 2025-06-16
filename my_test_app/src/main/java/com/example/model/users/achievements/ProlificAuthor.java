package com.example.model.users.achievements;

import jakarta.persistence.Entity;

@Entity
public class ProlificAuthor extends Achievements {

    private final String name = "Prolific Author—The user created five quizzes.";

    @Override
    public String getName() {
        return name;
    }
}
