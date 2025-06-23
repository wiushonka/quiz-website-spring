package com.example.model.users.activities;

import jakarta.persistence.Entity;

@Entity
public class NewAchivement extends FriendActivity {

    public NewAchivement() {}

    public NewAchivement(Long userId, Long achievementId, String username) {
        super(userId,null,null,achievementId,username);
    }

    @Override
    public String getActivity() {
        return "";
    }
}
