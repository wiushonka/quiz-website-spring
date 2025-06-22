package com.example.model.users.activities;

import com.example.model.users.User;
import jakarta.persistence.Entity;

@Entity
public class createdQuiz extends FriendActivity{
    public createdQuiz(){}

    public createdQuiz(Long userId, Long quizId,String username) {
        super(userId,quizId,null,null,username);
    }

    @Override
    public String getActivity() {
        return "QuizCreated";
    }
}
