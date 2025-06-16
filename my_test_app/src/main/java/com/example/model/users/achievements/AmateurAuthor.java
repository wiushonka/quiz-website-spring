package com.example.model.users.achievements;

import jakarta.persistence.Entity;

@Entity
public class AmateurAuthor extends Achievements {

    private final String name = "Amateur Author—The user created a quiz.";

    @Override
    public String getName() {
        return name;
    }
}
