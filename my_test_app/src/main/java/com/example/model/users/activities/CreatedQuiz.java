package com.example.model.users.activities;

import jakarta.persistence.Entity;

@Entity
public class CreatedQuiz extends FriendActivity{
    public CreatedQuiz(){}

    public CreatedQuiz(Long userId, Long quizId, String username) {
        super(userId,quizId,null,null,username);
    }

    @Override
    public String getActivity() {
        return "QuizCreated";
    }
}
