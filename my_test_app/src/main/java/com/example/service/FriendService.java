package com.example.service;

import com.example.model.users.FriendRequest;
import com.example.model.users.User;
import com.example.repos.FriendRequestRepo;
import com.example.repos.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FriendService {
    private final UserRepo userRepo;

    private final FriendRequestRepo friendRequestRepo;

    public FriendService(UserRepo userRepo, FriendRequestRepo friendRequestRepo) {
        this.userRepo = userRepo;
        this.friendRequestRepo = friendRequestRepo;
    }

    public FriendRequest sendFriendRequest(Long senderId, Long receiverId) {
        User sender   = userRepo.findById(senderId).orElseThrow();
        User receiver = userRepo.findById(receiverId).orElseThrow();

        for(User friend : sender.getFriends()) {
            if(friend.getId().equals(receiverId)) {
                return null;
            }
        }

        for (FriendRequest fr : sender.getSentRequests()) {
            if (fr.getReceiver().equals(receiver)) {
                return null;
            }
        }

        FriendRequest fr = new FriendRequest(sender, receiver);
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
        FriendRequest fr = friendRequestRepo.findById(requestId).orElse(null);
        if(fr == null) throw new RuntimeException("searching friend for non existing request");
        User receiver = fr.getReceiver();
        receiver.acceptFriendRequest(fr);
    }

    public void rejectFriendRequest(Long requestId) {
        FriendRequest fr = friendRequestRepo.findById(requestId).orElse(null);
        if(fr == null) throw new RuntimeException("searching friend for non existing request");
        User receiver = fr.getReceiver();
        receiver.rejectFriendRequest(fr);
    }

    public List<User> getFriendsList(Long userId) {
        User user = userRepo.findById(userId).orElse(null);
        if(user == null) throw new RuntimeException("searching friends for non existing user");
        return user.getFriends();
    }
}
