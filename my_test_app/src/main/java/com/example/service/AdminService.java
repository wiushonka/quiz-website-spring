package com.example.service;

import com.example.model.users.User;
import com.example.model.users.admin.Announcement;
import com.example.repos.AnnouncementRepo;
import com.example.repos.QuizRepo;
import com.example.repos.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashMap;

// TODO : ================== FINISH METHODS ================================

@Service
@Transactional
public class AdminService {

    private final UserRepo userRepository;

    private final QuizRepo quizRepository;

    private final AnnouncementRepo announcementRepository;

    public AdminService(UserRepo userRepository, QuizRepo quizRepository, AnnouncementRepo announcementRepository) {
        this.userRepository = userRepository;
        this.quizRepository = quizRepository;
        this.announcementRepository = announcementRepository;
    }

    public void deleteUser(Long userId) {
        this.userRepository.deleteById(userId);
    }

    public void removeQuiz(Long quizId) {
        this.clearQuizHistory(quizId);
        this.quizRepository.deleteById(quizId);
    }

    public void clearQuizHistory(Long quizId) {
        quizRepository.findById(quizId).ifPresent( quiz -> {
            quiz.clearHistory();
            quizRepository.save(quiz);
        });
    }


    public void promoteUser(User user) {
        User promotedUser = this.userRepository.findById(user.getId()).orElse(null);
        if (promotedUser != null) {
            promotedUser.promote();
        }
    }

    public void addAnnouncement(Announcement announcement) {
        announcementRepository.save(announcement);
    }

    public void deleteAnnouncement(Long announcementId) {
        announcementRepository.deleteById(announcementId);
    }

    // TODO : =========== probably add some other stats to show too ========
    public HashMap<String,Long> seeStats() {
        HashMap<String,Long> stats = new HashMap<>();

        Long userCount = userRepository.count();
        stats.put("userCount", userCount);

        long totalQuizsTaken = 0L;
        for (User u: userRepository.findAll()) {
            totalQuizsTaken += u.getUserHistory().size();
        }
        stats.put("totalQuizsTaken", totalQuizsTaken);

        return stats;
    }
}
