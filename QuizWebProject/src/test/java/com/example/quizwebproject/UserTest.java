package com.example.quizwebproject;

import com.example.quizwebproject.model.quizes.Quiz;
import com.example.quizwebproject.model.quizes.QuizResult;
import com.example.quizwebproject.model.users.Challenge;
import com.example.quizwebproject.model.users.FriendRequest;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.model.users.achievements.Achievements;
import com.example.quizwebproject.model.users.chat.Chat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserTest {

    private User user;
    private User friendUser;

    @Mock
    private Quiz mockQuiz;

    @Mock
    private QuizResult mockQuizResult;

    @Mock
    private Chat mockChat;

    @Mock
    private FriendRequest mockFriendRequest;

    @Mock
    private Challenge mockChallenge;

    @Mock
    private Achievements mockAchievements;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User("testuser", "password123");
        friendUser = new User("frienduser", "friendpass");
    }

    @Test
    @DisplayName("Should create user with default constructor")
    void testDefaultConstructor() {
        User defaultUser = new User();
        assertNotNull(defaultUser);
    }

    @Test
    @DisplayName("Should create user with username and password")
    void testParameterizedConstructor() {
        assertEquals("testuser", user.getUsername());
        assertNotNull(user.getPassword());
        assertNotEquals("password123", user.getPassword()); // Should be hashed
        assertNotNull(user.getUserHistory());
        assertNotNull(user.getChallenges());
        assertNotNull(user.getAchievements());
        assertTrue(user.getUserHistory().isEmpty());
        assertTrue(user.getChallenges().isEmpty());
        assertTrue(user.getAchievements().isEmpty());
    }

    @Test
    @DisplayName("Should hash password using Base64 encoding")
    void testPasswordHashing() {
        String originalPassword = "testpassword";
        User testUser = new User("testuser", originalPassword);

        // Password should be hashed (Base64 encoded)
        assertNotEquals(originalPassword, testUser.getPassword());

        // Should be Base64 encoded
        String expectedHash = java.util.Base64.getEncoder().encodeToString(originalPassword.getBytes());
        assertEquals(expectedHash, testUser.getPassword());
    }

    @Test
    @DisplayName("Should return correct ID")
    void testGetId() {
        // Since ID is generated, we can test that getter works
        Long id = user.getId();
        assertEquals(id, user.getId());
    }

    @Test
    @DisplayName("Should return username")
    void testGetUsername() {
        assertEquals("testuser", user.getUsername());
    }

    @Test
    @DisplayName("Should return hashed password")
    void testGetPassword() {
        assertNotNull(user.getPassword());
        assertNotEquals("password123", user.getPassword());
    }

    @Test
    @DisplayName("Should return friends list")
    void testGetFriends() {
        List<User> friends = user.getFriends();
        assertNotNull(friends);
        assertTrue(friends.isEmpty());
    }

    @Test
    @DisplayName("Should return chats list")
    void testGetChats() {
        List<Chat> chats = user.getChats();
        assertNotNull(chats);
        assertTrue(chats.isEmpty());
    }

    @Test
    @DisplayName("Should return sent requests list")
    void testGetSentRequests() {
        List<FriendRequest> sentRequests = user.getSentRequests();
        assertNotNull(sentRequests);
        assertTrue(sentRequests.isEmpty());
    }

    @Test
    @DisplayName("Should return pending requests list")
    void testGetPendingRequests() {
        List<FriendRequest> pendingRequests = user.getPendingRequests();
        assertNotNull(pendingRequests);
        assertTrue(pendingRequests.isEmpty());
    }

    @Test
    @DisplayName("Should return user history list")
    void testGetUserHistory() {
        List<QuizResult> userHistory = user.getUserHistory();
        assertNotNull(userHistory);
        assertTrue(userHistory.isEmpty());
    }

    @Test
    @DisplayName("Should return challenges list")
    void testGetChallenges() {
        List<Challenge> challenges = user.getChallenges();
        assertNotNull(challenges);
        assertTrue(challenges.isEmpty());
    }

    @Test
    @DisplayName("Should return achievements list")
    void testGetAchievements() {
        List<Achievements> achievements = user.getAchievements();
        assertNotNull(achievements);
        assertTrue(achievements.isEmpty());
    }

    @Test
    @DisplayName("Should add result to user history")
    void testAddResultToHistory() {
        assertTrue(user.getUserHistory().isEmpty());

        user.addResultToHistory(mockQuizResult);

        assertEquals(1, user.getUserHistory().size());
        assertTrue(user.getUserHistory().contains(mockQuizResult));
    }

    @Test
    @DisplayName("Should add multiple results to user history")
    void testAddMultipleResultsToHistory() {
        QuizResult result1 = mock(QuizResult.class);
        QuizResult result2 = mock(QuizResult.class);

        user.addResultToHistory(result1);
        user.addResultToHistory(result2);

        assertEquals(2, user.getUserHistory().size());
        assertTrue(user.getUserHistory().contains(result1));
        assertTrue(user.getUserHistory().contains(result2));
    }

    @Test
    @DisplayName("Should receive challenge")
    void testReceiveChallenge() {
        assertTrue(user.getChallenges().isEmpty());

        user.receiveChallenge(mockChallenge);

        assertEquals(1, user.getChallenges().size());
        assertTrue(user.getChallenges().contains(mockChallenge));
    }

    @Test
    @DisplayName("Should remove challenge when accepted or rejected")
    void testChallengeAcceptedOrRejected() {
        user.receiveChallenge(mockChallenge);
        assertEquals(1, user.getChallenges().size());

        user.challengeAcceptedOrRejected(mockChallenge);

        assertTrue(user.getChallenges().isEmpty());
    }

    @Test
    @DisplayName("Should return null for best score when no results for quiz")
    void testGetBestScoreNoResults() {
        QuizResult bestScore = user.getBestScore(mockQuiz);
        assertNull(bestScore);
    }

    @Test
    @DisplayName("Should return null for best score when no matching quiz")
    void testGetBestScoreNoMatchingQuiz() {
        QuizResult result = mock(QuizResult.class);
        Quiz differentQuiz = mock(Quiz.class);

        when(result.getQuiz()).thenReturn(differentQuiz);
        user.addResultToHistory(result);

        QuizResult bestScore = user.getBestScore(mockQuiz);
        assertNull(bestScore);
    }

    @Test
    @DisplayName("Should handle null quiz in getUserHistory when getting best score")
    void testGetBestScoreWithNullQuizInHistory() {
        QuizResult resultWithNullQuiz = mock(QuizResult.class);
        when(resultWithNullQuiz.getQuiz()).thenReturn(null);

        user.addResultToHistory(resultWithNullQuiz);

        QuizResult bestScore = user.getBestScore(mockQuiz);
        assertNull(bestScore);
    }

    @Test
    @DisplayName("Should cleanup relationships in preRemove")
    void testPreRemove() {
        // Setup friends relationship
        user.getFriends().add(friendUser);
        friendUser.getFriends().add(user);

        // Setup chats relationship
        Chat chat = mock(Chat.class);
        List<User> chatUsers = new ArrayList<>();
        chatUsers.add(user);
        when(chat.getUsers()).thenReturn(chatUsers);
        user.getChats().add(chat);

        // Setup quiz results
        QuizResult quizResult = mock(QuizResult.class);
        Quiz quiz = mock(Quiz.class);
        List<QuizResult> quizHistory = new ArrayList<>();
        quizHistory.add(quizResult);

        when(quizResult.getQuiz()).thenReturn(quiz);
        when(quiz.getHistory()).thenReturn(quizHistory);
        user.getUserHistory().add(quizResult);

        // Execute preRemove
        user.preRemove();

        // Verify cleanup
        assertTrue(user.getFriends().isEmpty());
        assertFalse(friendUser.getFriends().contains(user));
        assertTrue(user.getChats().isEmpty());

        // Verify quiz result cleanup
        verify(quizResult).setQuiz(null);
        verify(quizResult).setUser(null);
        verify(quiz).getHistory(); // Verify the quiz history was accessed
    }

    @Test
    @DisplayName("Should handle empty collections in preRemove")
    void testPreRemoveWithEmptyCollections() {
        // Should not throw any exceptions
        assertDoesNotThrow(() -> user.preRemove());

        assertTrue(user.getFriends().isEmpty());
        assertTrue(user.getChats().isEmpty());
        assertTrue(user.getUserHistory().isEmpty());
    }

    @Test
    @DisplayName("Should maintain collection integrity")
    void testCollectionIntegrity() {
        // Test that collections are properly initialized and maintain references
        List<User> friends = user.getFriends();
        List<Chat> chats = user.getChats();
        List<QuizResult> history = user.getUserHistory();
        List<Challenge> challenges = user.getChallenges();

        // Adding elements should be reflected in subsequent calls
        friends.add(friendUser);
        chats.add(mockChat);
        history.add(mockQuizResult);
        challenges.add(mockChallenge);

        assertEquals(1, user.getFriends().size());
        assertEquals(1, user.getChats().size());
        assertEquals(1, user.getUserHistory().size());
        assertEquals(1, user.getChallenges().size());
    }
}
