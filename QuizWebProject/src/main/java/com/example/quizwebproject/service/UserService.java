package com.example.quizwebproject.service;

import com.example.quizwebproject.model.quizes.Quiz;
import com.example.quizwebproject.model.quizes.QuizResult;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.model.users.achievements.*;
import com.example.quizwebproject.model.users.chat.Chat;
import com.example.quizwebproject.repos.AchievementsRepo;
import com.example.quizwebproject.repos.ChatRepo;
import com.example.quizwebproject.repos.QuizRepo;
import com.example.quizwebproject.repos.UserRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final ChatRepo chatRepo;
    private final UserRepo userRepo;
    private final QuizRepo quizRepo;
    private final AchievementsRepo achievementsRepo;

    public UserService(ChatRepo chatRepo, UserRepo userRepo, QuizRepo quizRepo, AchievementsRepo achievementsRepo) {
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
        this.quizRepo = quizRepo;
        this.achievementsRepo = achievementsRepo;
    }

    public void addUserToChat(@NotNull Long chatId, @NotNull Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user == null) throw new RuntimeException("User not found");
        Chat chat = chatRepo.findById(chatId).orElse(null);
        if(chat == null) throw new RuntimeException("Chat not found");
        user.getChats().add(chat);
    }

    public User getUserById(@NotNull Long id) {
        return userRepo.findById(id).orElse(null);
    }

    public boolean isValidPass(@NotNull User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        User DBUser = userRepo.findByUsername(username);
        if(DBUser != null) {
            return DBUser.getPassword().equals(password);
        }
        return false;
    }

    public boolean userExists(@NotNull User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        User DBUser = userRepo.findByUsername(username);
        return DBUser != null;
    }

    public void addNewUser(@NotNull User user) {
        userRepo.save(user);
    }

    public User getUser(@NotNull String username) {
        return userRepo.findByUsername(username);
    }

    public List<Chat> getUserChats(@NotNull Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user == null) throw new RuntimeException("User not found");
        return user.getChats();
    }

    public List<User> getUserFriends(@NotNull Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user == null) throw new RuntimeException("User not found");
        return user.getFriends();
    }

    public User getUserByUsername(String username){
        return userRepo.findByUsername(username);
    }

    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    public void checkAchivements(@NotNull Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user == null) throw new RuntimeException("User not found");

        List<Quiz> userQuizs = quizRepo.findQuizByAuthorId(user.getId());
        List<Achievements> achis = achievementsRepo.findUserAchievements(userId, Pageable.unpaged());

        boolean isAmateurAuthor = achis.stream().anyMatch(elem -> elem instanceof AmateurAuthor );
        if(!isAmateurAuthor && !userQuizs.isEmpty()) {
            Achievements ach = new AmateurAuthor(user);
            achievementsRepo.save(ach);
            user.getAchievements().add(ach);
        }

        boolean isProlificAuthor = achis.stream().anyMatch(elem -> elem instanceof ProlificAuthor);
        if(!isProlificAuthor && userQuizs.size() >= 5) {
            Achievements ach = new ProlificAuthor(user);
            achievementsRepo.save(ach);
            user.getAchievements().add(ach);
        }

        boolean isProdigiusAuthor = achis.stream().anyMatch(elem -> elem instanceof ProdigiousAuthor);
        if(!isProdigiusAuthor && userQuizs.size() >= 10) {
            Achievements ach = new ProdigiousAuthor(user);
            achievementsRepo.save(ach);
            user.getAchievements().add(ach);
        }

        boolean isQuizMachine = achis.stream().anyMatch(elem -> elem instanceof QuizMachine);
        if(!isQuizMachine && user.getUserHistory().size() >= 10) {
            Achievements ach = new QuizMachine(user);
            achievementsRepo.save(ach);
            user.getAchievements().add(ach);
        }
    }

    public void givePracticeMakesPerfectToUser(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user == null) throw new RuntimeException("User not found");
        Achievements ach = new PracticeMakesPerfect(user);
        achievementsRepo.save(ach);
        user.getAchievements().add(ach);
    }
}
