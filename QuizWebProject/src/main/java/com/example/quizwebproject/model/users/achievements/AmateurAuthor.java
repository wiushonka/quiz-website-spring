package com.example.quizwebproject.model.users.achievements;

import com.example.quizwebproject.model.users.User;
import jakarta.persistence.Entity;

@Entity
public class AmateurAuthor extends Achievements {

    public AmateurAuthor() {
        super();
    }

    public AmateurAuthor(User user) {
        super(user);
    }

    @Override
    public String getName() {
        return "Amateur Author — The user created their first quiz.";
    }
}
