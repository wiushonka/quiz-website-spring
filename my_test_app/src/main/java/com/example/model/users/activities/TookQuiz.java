package com.example.model.users.activities;

import jakarta.persistence.Entity;

@Entity
public class TookQuiz extends FriendActivity{

    public TookQuiz() {}

    public TookQuiz(Long userId, Long quizResultId, String username) {
        super(userId,null,quizResultId,null,username);
    }

    @Override
    public String getActivity() {
        return "tookQuiz";
    }
}
