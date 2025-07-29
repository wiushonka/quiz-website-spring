package com.example.quizwebproject.model.users.achievements;

import com.example.quizwebproject.model.users.User;
import jakarta.persistence.Entity;

@Entity
public class PracticeMakesPerfect extends Achievements {

    public PracticeMakesPerfect() {
        super();
    }

    public PracticeMakesPerfect(User user) {
        super(user);
    }

    @Override
    public String getName() {
        return "practice makes perfect";
    }
}
