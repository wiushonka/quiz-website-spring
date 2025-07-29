package com.example.quizwebproject.model.users.achievements;

import com.example.quizwebproject.model.users.User;
import jakarta.persistence.Entity;

@Entity
public class ProdigiousAuthor extends Achievements {

    public ProdigiousAuthor() {
        super();
    }

    public ProdigiousAuthor(User user) {
        super(user);
    }

    @Override
    public String getName() {
        return "Prodigious Author — The user created 10 or more quizzes.";
    }
}
