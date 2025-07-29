package com.example.quizwebproject.service;

import com.example.quizwebproject.DTOs.ChallengeAcceptanceDTO;
import com.example.quizwebproject.model.quizes.Quiz;
import com.example.quizwebproject.model.quizes.QuizResult;
import com.example.quizwebproject.model.users.Challenge;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.repos.ChallengesRepo;
import com.example.quizwebproject.repos.QuizRepo;
import com.example.quizwebproject.repos.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChallengeService {

    private final ChallengesRepo chalRepo;

    private final UserRepo userRepo;

    private final QuizRepo quizRepo;

    public ChallengeService(ChallengesRepo chalRepo, UserRepo userRepo, QuizRepo quizRepo) {
        this.chalRepo = chalRepo;
        this.userRepo = userRepo;
        this.quizRepo = quizRepo;
    }

    public void sendChallenge(Long senderId, Long receiverId, Long quizId) {
        User sender = userRepo.findById(senderId).orElse(null);
        User receiver = userRepo.findWithLock(receiverId).orElse(null);
        Quiz quiz = quizRepo.findById(quizId).orElse(null);
        if(sender == null || receiver == null || quiz == null) throw new RuntimeException("User not found");

        QuizResult bestScore = sender.getBestScore(quiz);
        for(Challenge chal : receiver.getChallenges()) {
            if (chal.getQuiz().getId().equals(quiz.getId()) &&
                    chal.getSender().getId().equals(sender.getId()) &&
                    chal.getReceiver().getId().equals(receiver.getId())) {
                return;
            }
        }

        Challenge challenge = new Challenge(sender,receiver,quiz,bestScore);
        receiver.getChallenges().add(challenge);
    }

    public ChallengeAcceptanceDTO acceptChallenge(Long chalId) {
        Challenge chal = chalRepo.findById(chalId).orElseThrow(() -> new RuntimeException("Challenge not found"));
        Long receiverId = chal.getReceiver().getId();
        User receiver = userRepo.findWithLock(receiverId).orElse(null);
        if(receiver == null) throw new RuntimeException("User not found");
        receiver.getChallenges().remove(chal);
        chalRepo.delete(chal);
        return new ChallengeAcceptanceDTO(chal.getQuiz(),chal.getSenderHS());
    }

    public void rejectChallenge(Long chalId) {
        Challenge chal = chalRepo.findById(chalId).orElse(null);
        if(chal == null) throw new RuntimeException("Challenge not found");
        Long receiverId = chal.getReceiver().getId();
        User receiver = userRepo.findWithLock(receiverId).orElse(null);
        if(receiver == null) throw new RuntimeException("User not found");
        receiver.getChallenges().remove(chal);
        chalRepo.delete(chal);
    }

    public List<Challenge> getAllChallenges(Long userId) {
        return chalRepo.findByReceiverId(userId);
    }
}
