package com.example.quizwebproject.model.users.admin;

import com.example.quizwebproject.model.users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Announcement {

    // TODO : ====== SOME EXTRA CONTENT CAN BE ADDED, FOR EXAMPLE IMAGES SUPPORT OR SOMTH ======

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    private String title;

    @Lob
    private String content;

    private LocalDateTime dateTime;

    @ManyToOne
    private User author;

    public Announcement() {}

    public Announcement(User author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
    }

    @PrePersist
    protected void onCreate() {
        dateTime = LocalDateTime.now();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Long getId() {
        return id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthorName() {
        if (author != null) {
            return author.getUsername();
        }else {
            throw new IllegalStateException("Author is not set");
        }
    }
}
