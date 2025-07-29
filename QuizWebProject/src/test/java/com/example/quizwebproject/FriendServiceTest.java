package com.example.quizwebproject;

import com.example.quizwebproject.model.users.FriendRequest;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.repos.FriendRequestRepo;
import com.example.quizwebproject.repos.UserRepo;
import com.example.quizwebproject.service.FriendService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.*;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class FriendServiceTest {

    @Mock
    FriendRequestRepo reqRepo;
    @Mock
    UserRepo userRepo;
    @InjectMocks
    FriendService svc;

    User a,b;

    private static void id(Object o,long v) {
        try { Field f=o.getClass().getDeclaredField("id"); f.setAccessible(true); f.set(o,v);}
        catch(Exception e){throw new RuntimeException(e);}
    }

    @BeforeEach
    void init() {
        a=new User("a","p");
        b=new User("b","p");
        id(a,1L); id(b,2L);
    }

    @Test
    void sendRequestCreates() {
        Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(a));
        Mockito.when(userRepo.findById(2L)).thenReturn(Optional.of(b));
        FriendRequest fr = svc.sendFriendRequest(1L,2L);
        assertThat(fr).isNotNull();
        assertThat(a.getSentRequests()).contains(fr);
        assertThat(b.getPendingRequests()).contains(fr);
    }

    @Test
    void duplicateReturnsNull() {
        Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(a));
        Mockito.when(userRepo.findById(2L)).thenReturn(Optional.of(b));
        svc.sendFriendRequest(1L,2L);
        assertThat(svc.sendFriendRequest(1L,2L)).isNull();
    }

    @Test
    void acceptLinksUsers() {
        FriendRequest fr=new FriendRequest(a,b); id(fr,9L);
        Mockito.when(reqRepo.findWithLock(9L)).thenReturn(Optional.of(fr));
        Mockito.when(userRepo.findWithLock(1L)).thenReturn(Optional.of(a));
        Mockito.when(userRepo.findWithLock(2L)).thenReturn(Optional.of(b));
        svc.acceptFriendRequest(9L);
        assertThat(a.getFriends().stream().map(User::getId)).contains(b.getId());
        assertThat(b.getFriends().stream().map(User::getId)).contains(a.getId());
        assertThat(fr.getResult()).isTrue();
    }

    @Test
    void rejectRemoves() {
        FriendRequest fr=new FriendRequest(a,b); id(fr,7L);
        b.getPendingRequests().add(fr); a.getSentRequests().add(fr);
        Mockito.when(reqRepo.findWithLock(7L)).thenReturn(Optional.of(fr));
        Mockito.when(userRepo.findWithLock(1L)).thenReturn(Optional.of(a));
        Mockito.when(userRepo.findWithLock(2L)).thenReturn(Optional.of(b));
        svc.rejectFriendRequest(7L);
        assertThat(a.getSentRequests()).isEmpty();
        assertThat(b.getPendingRequests()).isEmpty();
        assertThat(fr.getResult()).isFalse();
    }

    @Test
    void endFriendshipRemovesBoth() {
        a.getFriends().add(b); b.getFriends().add(a);
        Mockito.when(userRepo.findById(1L)).thenReturn(Optional.of(a));
        Mockito.when(userRepo.findById(2L)).thenReturn(Optional.of(b));
        svc.endFriendship(1L,2L);
        assertThat(a.getFriends()).isEmpty();
        assertThat(b.getFriends()).isEmpty();
    }
}
