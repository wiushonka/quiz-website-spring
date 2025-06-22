package com.example.model.users.activities;

import jakarta.persistence.Entity;

@Entity
public class tookQuiz extends FriendActivity{

    public tookQuiz() {}

    public tookQuiz(Long userId, Long quizResultId,String username) {
        super(userId,null,quizResultId,null,username);
    }

    @Override
    public String getActivity() {
        return "tookQuiz";
    }
}
