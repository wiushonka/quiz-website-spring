package com.example.model.users.activities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class FriendActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long userId;

    private Long quizId;

    private Long quizResultId;

    private Long achivementId;

    private LocalDateTime createdAt;

    private String username;

    public FriendActivity(){}

    public FriendActivity(Long userId, Long quizId, Long quizResultId, Long achivementId,String username) {
        this.userId = userId;
        this.quizId = quizId;
        this.quizResultId = quizResultId;
        this.achivementId = achivementId;
        this.username = username;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public String getUsername() { return username; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public Long getUserId() {return userId;}

    public Long getQuizId() {return quizId;}

    public Long getQuizResultId() {return quizResultId;}

    public Long getAchivementId() {return achivementId;}

    public Long getId() { return id; }

    public abstract String getActivity();
}
