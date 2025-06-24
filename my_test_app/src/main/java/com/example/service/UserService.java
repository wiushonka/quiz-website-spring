package com.example.service;


import com.example.model.users.User;
import com.example.model.users.chat.Chat;
import com.example.repos.ChatRepo;
import com.example.repos.UserRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepo userRepo;

    private final ChatRepo chatRepo;

    public UserService(UserRepo userRepo, ChatRepo chatRepo) {
        this.chatRepo = chatRepo;
        this.userRepo = userRepo;
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

    public User getUserById(@NotNull Long id) {
        return userRepo.findById(id).orElse(null);
    }

    public void addUserToChat(@NotNull Long chatId, @NotNull Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user == null) throw new RuntimeException("User not found");
        Chat chat = chatRepo.findById(chatId).orElse(null);
        if(chat == null) throw new RuntimeException("Chat not found");
        user.getChats().add(chat);
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
}
