package com.example.repos;

import com.example.model.users.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRequestRepo extends JpaRepository<FriendRequest, Long> { }
