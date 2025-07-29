package com.example.quizwebproject;

import com.example.quizwebproject.repos.FriendActivityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class NewAchievementTest {

    @Autowired
    private FriendActivityRepo repository;
}
