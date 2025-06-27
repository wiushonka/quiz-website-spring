package com.example.model.users.activities;

import jakarta.persistence.Entity;

@Entity
public class QuizCreation extends FriendActivity{
    public QuizCreation(){}

    public QuizCreation(Long userId, Long quizId, String username) {
        super(userId,quizId,null,null,username);
    }

    @Override
    public String getActivity() {
        return "QuizCreated";
    }
}
