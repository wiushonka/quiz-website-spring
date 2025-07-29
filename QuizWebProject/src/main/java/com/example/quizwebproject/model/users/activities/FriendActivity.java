package com.example.quizwebproject.model.users.activities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class FriendActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long quizId;
    private Long quizResultId;
    private Long achievementId;
    private String username;
    private LocalDateTime createdAt;

    public FriendActivity() {}

    public FriendActivity(Long userId, Long quizId, Long quizResultId, Long achievementId, String username) {
        this.userId = userId;
        this.quizId = quizId;
        this.quizResultId = quizResultId;
        this.achievementId = achievementId;
        this.username = username;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getQuizId() {
        return quizId;
    }

    public Long getQuizResultId() {
        return quizResultId;
    }

    public Long getAchievementId() {
        return achievementId;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public abstract String getActivity();
}
