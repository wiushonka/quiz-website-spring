package com.example.model.users;

import com.example.model.quizes.Quiz;
import com.example.model.quizes.QuizResult;
import jakarta.persistence.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    private List<User> friends = new ArrayList<>();

    @ManyToMany(mappedBy = "users")
    private List<Chat> chats = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> sentRequests = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> pendingRequests = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuizResult> userHistory;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Challenge> challenges;

    @Column(nullable = false)
    private boolean admin;

    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = hashPassword(password);
        userHistory = new ArrayList<>();
        challenges = new ArrayList<>();
        admin = false;
    }

    public void promote() {
        this.admin = true;
    }

    public void demote() {
        this.admin = false;
    }

    // todo: remove this old method, was only for testing
    public FriendRequest sendFriendRequest(User receiver) {
        for (FriendRequest fr : this.sentRequests) {
            if (fr.getReceiver().equals(receiver)) {
                return null;
            }
        }

        FriendRequest fr = new FriendRequest(this, receiver);
        this.sentRequests.add(fr);
        receiver.pendingRequests.add(fr);
        return fr;
    }

    public void acceptFriendRequest(FriendRequest request) {
        if (this.pendingRequests.contains(request)) {
            request.setResult(true);
            this.friends.add(request.getSender());
            request.getSender().friends.add(this);
            this.pendingRequests.remove(request);
            request.getSender().sentRequests.remove(request);
        }
    }

    public void rejectFriendRequest(FriendRequest request) {
        if (this.pendingRequests.contains(request)) {
            request.setResult(false);
            this.pendingRequests.remove(request);
            request.getSender().sentRequests.remove(request);
        }
    }

    public void removeFriend(User friend) {
        this.friends.remove(friend);
        friend.friends.remove(this);
    }

    public Long getId() { return id; }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public List<User> getFriends() { return friends; }

    public List<Chat> getChats() { return chats; }

    public List<FriendRequest> getSentRequests() { return sentRequests; }

    @PreRemove
    public void preRemove() {
        List<User> frs = new ArrayList<>(friends);
        for (User friend : frs) {
            friend.getFriends().remove(this);
        }
        friends.clear();

        List<Chat> ch = new ArrayList<>(chats);
        for (Chat chat : ch) {
            chat.getUsers().remove(this);
        }
        chats.clear();

        List<QuizResult> qrs = this.userHistory;
        for (QuizResult qr : qrs) {
            List<QuizResult> quizQrs = qr.getQuiz().getHistory();
            quizQrs.remove(qr);
            qr.setQuiz(null);
            qr.setUser(null);
        }
    }

    public List<FriendRequest> getPendingRequests() { return pendingRequests; }

    public void sendMessage(String message, @NotNull Chat chat) { chat.addMessage(this.username + ":>" + "\n" + message); }

    public List<QuizResult> getUserHistory() { return userHistory; }

    public void addResultToHistory(QuizResult result) {
        userHistory.add(result);
    }

    public void challengeAcceptedOrRejected(Challenge challenge) {
        challenges.remove(challenge);
    }

    public void receiveChallenge(Challenge challenge) {
        challenges.add(challenge);
    }

    public Challenge challengeUser(User receiver,Quiz quiz) {
        Challenge ch = new Challenge(this,receiver,quiz);
        receiver.receiveChallenge(ch);
        return ch;
    }

    public List<Challenge> getChallenges() { return challenges; }

    // TODO : ================== IMPLEMENT METHODS ======================

    public QuizResult getBestScore(Quiz quiz) {
        // TODO : MUST WRITE COMPARATOR FOR QUIZRESULT  CLASS
        return null;
    }

    private String hashPassword(String pas) {
        // TODO: Replace with real hashing
        return pas;
    }
}


