package com.example.quizwebproject.model.users.activities;

import jakarta.persistence.Entity;

@Entity
public class NewAchievement extends FriendActivity {

    public NewAchievement() {}

    public NewAchievement(Long userId, Long achievementId, String username) {
        super(userId, null, null, achievementId, username);
    }

    @Override
    public String getActivity() {
        return "Unlocked a new achievement";
    }
}
