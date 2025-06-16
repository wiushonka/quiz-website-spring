package com.example.service;


import com.example.model.users.User;
import com.example.repos.UserRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
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
}
