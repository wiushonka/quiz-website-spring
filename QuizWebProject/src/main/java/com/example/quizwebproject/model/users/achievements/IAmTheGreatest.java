package com.example.quizwebproject.model.users.achievements;

import com.example.quizwebproject.model.users.User;
import jakarta.persistence.Entity;

@Entity
public class IAmTheGreatest extends Achievements{

    public IAmTheGreatest() {
        super();
    }

    public IAmTheGreatest(User user) {
        super(user);
    }

    @Override
    public String getName() {
        return "I'm the Greatest — The user achieved the highest score in a quiz.";
    }
}
