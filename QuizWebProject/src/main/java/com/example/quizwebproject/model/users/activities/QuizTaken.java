package com.example.quizwebproject.model.users.activities;

import jakarta.persistence.Entity;

@Entity
public class QuizTaken extends FriendActivity{

    public QuizTaken() {}

    public QuizTaken(Long userId, Long quizResultId, String username) {
        super(userId, null, quizResultId, null, username);
    }

    @Override
    public String getActivity() {
        return "";
    }
}
