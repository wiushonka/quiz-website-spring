package com.example.service;

import com.example.model.quizes.Quiz;
import com.example.model.users.admin.Announcement;
import com.example.repos.AnnouncementRepo;
import com.example.repos.ChatRepo;
import com.example.repos.QuizRepo;
import com.example.repos.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO : ================== ADD METHODS ================================

@Service
public class HomepageService {
    private final QuizRepo quizRepo;

    private final AnnouncementRepo announcementRepo;

    private final UserRepo userRepo;

    private final ChatRepo chatRepo;

    public HomepageService(QuizRepo quizRepo, AnnouncementRepo announcementRepo, UserRepo userRepo, ChatRepo chatRepo ) {
        this.quizRepo = quizRepo;
        this.announcementRepo = announcementRepo;
        this.userRepo = userRepo;
        this.chatRepo = chatRepo;
    }

    public List<Announcement> getAnnouncements() {
        return this.announcementRepo.findAll();
    }

    public List<Quiz> popularQuizs() {
        return this.quizRepo.findAll();
    }
}
