package com.example.quizwebproject.repos;

import com.example.quizwebproject.model.users.FriendRequest;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRequestRepo extends JpaRepository<FriendRequest, Long>{
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT fr FROM FriendRequest fr WHERE fr.id = :recId")
    Optional<FriendRequest> findWithLock(@Param("recId") Long recId);
}
