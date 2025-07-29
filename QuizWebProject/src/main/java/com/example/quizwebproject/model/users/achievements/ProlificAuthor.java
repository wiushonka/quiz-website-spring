package com.example.quizwebproject.model.users.achievements;

import com.example.quizwebproject.model.users.User;
import jakarta.persistence.Entity;

@Entity
public class ProlificAuthor extends Achievements {

    public ProlificAuthor() {
        super();
    }

    public ProlificAuthor(User user) {
        super(user);
    }

    @Override
    public String getName() {
        return "Prolific Author — The user created five quizzes.";
    }
}
