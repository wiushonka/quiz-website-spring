package com.example.model.users.activities;

import jakarta.persistence.Entity;

@Entity
public class newAchivement extends FriendActivity {

    public newAchivement() {}

    public newAchivement(Long userId, Long achievementId,String username) {
        super(userId,null,null,achievementId,username);
    }

    @Override
    public String getActivity() {
        return "";
    }
}
