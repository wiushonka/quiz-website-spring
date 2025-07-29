package com.example.quizwebproject;

import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.model.users.achievements.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class AchievementsTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("tester", "password");
    }

    @Test
    public void testAchievementNames() {
        List<Achievements> all = List.of(
                new AmateurAuthor(),
                new ProlificAuthor(),
                new ProdigiousAuthor(),
                new QuizMachine(),
                new IAmTheGreatest(),
                new PracticeMakesPerfect()
        );

        assertEquals("Amateur Author — The user created their first quiz.",
                all.get(0).getName());
        assertEquals("Prolific Author — The user created five quizzes.",
                all.get(1).getName());
        assertEquals("Prodigious Author — The user created 10 or more quizzes.",
                all.get(2).getName());
        assertEquals("Quiz Machine — The user completed 50 or more quizzes.",
                all.get(3).getName());
        assertEquals("I'm the Greatest — The user achieved the highest score in a quiz.",
                all.get(4).getName());
        assertEquals("practice makes perfect",
                all.get(5).getName());
    }

    @Test
    public void testCreateAchievementWithUser() {
        Achievements ach = new AmateurAuthor(user);
        assertNotNull(ach);
        assertEquals(user, ach.getUser());
    }

    @Test
    public void testAchievementDateTimeIsSetOnCreate() throws Exception {
        Achievements ach = new ProlificAuthor();

        Method onCreate = Achievements.class.getDeclaredMethod("onCreate");
        onCreate.setAccessible(true);
        onCreate.invoke(ach);

        LocalDateTime ts = ach.getDateTime();
        assertNotNull(ts);
        assertTrue(ts.isAfter(LocalDateTime.now().minus(1, ChronoUnit.SECONDS)));
        assertTrue(ts.isBefore(LocalDateTime.now().plus(1, ChronoUnit.SECONDS)));
    }

    @Test
    public void testAchievementsAreLinkedToUser() {
        Achievements ach1 = new QuizMachine(user);
        Achievements ach2 = new PracticeMakesPerfect(user);

        assertEquals(user, ach1.getUser());
        assertEquals(user, ach2.getUser());
    }

    @Test
    public void testAchievementBehaviorWhenUserMissing() {
        Achievements ach = new ProdigiousAuthor();
        assertNull(ach.getUser());
        assertNull(ach.getId());
    }
}
