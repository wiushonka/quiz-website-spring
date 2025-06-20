package com.example.model.users;

import com.example.model.quizes.Quiz;
import com.example.model.quizes.QuizResult;
import com.example.model.users.chat.Chat;
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
    @JoinTable(
            name = "user_friends",
            joinColumns =  @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id"),
            uniqueConstraints = @UniqueConstraint(
                    columnNames = { "user_id", "friend_id" }
            )
    )
    private List<User> friends = new ArrayList<>();

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private List<Chat> chats = new ArrayList<>();

    @OneToMany(mappedBy = "sender", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> sentRequests = new ArrayList<>();

    @OneToMany(mappedBy = "receiver", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FriendRequest> pendingRequests = new ArrayList<>();
                                                                            // TODO : can prob change this
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<QuizResult> userHistory;

    @OneToMany(cascade = CascadeType.ALL)
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

    public void acceptFriendRequest(FriendRequest request) {
        if (!this.pendingRequests.contains(request)) return;

        User sender = request.getSender();

        if (this.friends.contains(sender) && sender.getFriends().contains(this)) {
            pendingRequests.remove(request);
            return;
        }

        if (this.friends.contains(sender) || sender.getFriends().contains(this)) {
            throw new RuntimeException("Friendship data is inconsistent between users.");
        }

        request.setResult(true);

        if(this.id > sender.getId()) {
            request.getSender().friends.add(this);
            this.friends.add(request.getSender());
        }else {
            this.friends.add(request.getSender());
            request.getSender().friends.add(this);
        }

        this.pendingRequests.remove(request);
        request.getSender().sentRequests.remove(request);
    }

    public void rejectFriendRequest(FriendRequest request) {
        if (this.pendingRequests.contains(request)) {
            request.setResult(false);
            this.pendingRequests.remove(request);
            request.getSender().sentRequests.remove(request);
        }
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


