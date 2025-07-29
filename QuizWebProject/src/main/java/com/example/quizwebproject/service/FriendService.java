package com.example.quizwebproject.service;

import com.example.quizwebproject.model.users.FriendRequest;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.repos.FriendRequestRepo;
import com.example.quizwebproject.repos.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
public class FriendService {

    private final FriendRequestRepo friendRequestRepo;
    private final UserRepo userRepo;

    public FriendService(FriendRequestRepo friendRequestRepo, UserRepo userRepo) {
        this.friendRequestRepo = friendRequestRepo;
        this.userRepo = userRepo;
    }

    public FriendRequest sendFriendRequest(Long senderId, Long receiverId) {
        User sender = (User)this.userRepo.findById(senderId).orElseThrow();
        User receiver = (User)this.userRepo.findById(receiverId).orElseThrow();
        Iterator var6 = sender.getFriends().iterator();

        while(var6.hasNext()) {
            User friend = (User)var6.next();
            if (friend.getId().equals(receiverId)) {
                return null;
            }
        }

        var6 = sender.getSentRequests().iterator();

        FriendRequest fr;
        while(var6.hasNext()) {
            fr = (FriendRequest)var6.next();
            if (fr.getReceiver().equals(receiver)) {
                return null;
            }
        }

        fr = new FriendRequest(sender, receiver);
        sender.getSentRequests().add(fr);
        receiver.getPendingRequests().add(fr);
        return fr;
    }
    public List<FriendRequest> getFriendRequests(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user == null) throw new RuntimeException("searching friends for non existing user");
        return user.getPendingRequests();
    }

    public void acceptFriendRequest(Long requestId) {
        FriendRequest fr = friendRequestRepo.findWithLock(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));
        Long senderId = fr.getSender().getId();
        User sender = userRepo.findWithLock(senderId).orElseThrow(() -> new RuntimeException("user not found"));
        Long receiverId = fr.getReceiver().getId();
        User receiver = userRepo.findWithLock(receiverId).orElseThrow(() -> new RuntimeException("user not found"));

        if (sender == null || receiver == null)
            throw new RuntimeException("Sender or receiver is null");

        if (!receiver.getPendingRequests().contains(fr)) return;

        if (receiver.getFriends().contains(sender) && sender.getFriends().contains(receiver)) {
            receiver.getPendingRequests().remove(fr);
            return;
        }

        if (receiver.getFriends().contains(sender) || sender.getFriends().contains(receiver)) {
            throw new RuntimeException("Friendship data is inconsistent between users.");
        }

        fr.setResult(true);
        sender.getFriends().add(receiver);
        receiver.getFriends().add(sender);

        receiver.getPendingRequests().remove(fr);
        sender.getSentRequests().remove(fr);
    }

    public void rejectFriendRequest(Long requestId) {
        FriendRequest fr = friendRequestRepo.findWithLock(requestId)
                .orElseThrow(() -> new RuntimeException("Friend request not found"));

        Long senderId = fr.getSender().getId();
        User sender = userRepo.findWithLock(senderId).orElseThrow(() -> new RuntimeException("user not found"));
        Long receiverId = fr.getReceiver().getId();
        User receiver = userRepo.findWithLock(receiverId).orElseThrow(() -> new RuntimeException("user not found"));

        if (sender == null || receiver == null)
            throw new RuntimeException("Sender or receiver is null");

        if (receiver.getPendingRequests().contains(fr)) {
            fr.setResult(false);
            receiver.getPendingRequests().remove(fr);
            sender.getSentRequests().remove(fr);
        }
    }

    public List<User> getFriendsList(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user == null) throw new RuntimeException("searching friends for non existing user");
        return user.getFriends();
    }

    public void endFriendship(Long remover, Long removed) {
        User removerUser = userRepo.findById(remover).orElse(null);
        User removedUser = userRepo.findById(removed).orElse(null);
        if(removedUser == null || removerUser == null) throw new RuntimeException("User not found");

        removerUser.getFriends().remove(removedUser);
        removedUser.getFriends().remove(removerUser);
    }

    public List<User> getNonFriendUsers(Long userId) {
        List<User> allUsers = userRepo.findAll();
        User user = userRepo.findById(userId).orElse(null);
        if(user == null) throw new RuntimeException("searching non friends for non existing user");
        List<User> friends = user.getFriends();
        List<User> nonFriends = new ArrayList<>();
        for(User u:allUsers) {
            if(!friends.contains(u) && !u.getId().equals(userId)) nonFriends.add(u);
        }
        return nonFriends;
    }
}

