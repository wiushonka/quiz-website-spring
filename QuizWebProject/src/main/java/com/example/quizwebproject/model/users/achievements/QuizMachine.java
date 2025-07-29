package com.example.quizwebproject.model.users.achievements;

import com.example.quizwebproject.model.users.User;
import jakarta.persistence.Entity;

@Entity
public class QuizMachine extends Achievements{

    public QuizMachine() {
        super();
    }

    public QuizMachine(User user) {
        super(user);
    }

    @Override
    public String getName() {
        return "Quiz Machine — The user completed 50 or more quizzes.";
    }
}
