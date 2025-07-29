package com.example.quizwebproject;

import com.example.quizwebproject.model.users.FriendRequest;
import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.service.FriendService;
import com.example.quizwebproject.service.HomepageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = com.example.quizwebproject.controller.FriendController.class)
public class FriendControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendService friendService;

    @MockBean
    private HomepageService homepageService;

    private User mockUser;

    @BeforeEach
    public void setup() {
        mockUser = new User("john", "password");
        try {
            java.lang.reflect.Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(mockUser, 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testSendFriendRequest_whenUserNotLoggedIn_redirectsToLogin() throws Exception {
        mockMvc.perform(post("/friend/send")
                        .param("receiverId", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void testSendFriendRequest_success() throws Exception {
        FriendRequest friendRequest = new FriendRequest(mockUser, new User("receiver", "pass"));
        when(friendService.sendFriendRequest(anyLong(), anyLong())).thenReturn(friendRequest);

        mockMvc.perform(post("/friend/send")
                        .param("receiverId", "2")
                        .sessionAttr("user", mockUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/2"))
                .andExpect(flash().attribute("newFriendRequestSent", true));
    }

    @Test
    public void testSendFriendRequest_failure() throws Exception {
        when(friendService.sendFriendRequest(anyLong(), anyLong())).thenReturn(null);

        mockMvc.perform(post("/friend/send")
                        .param("receiverId", "2")
                        .sessionAttr("user", mockUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/2"))
                .andExpect(flash().attribute("newFriendRequestSent", false));
    }

    @Test
    public void testAcceptFriendRequest_redirectsHomepage() throws Exception {
        mockMvc.perform(post("/friend/accept")
                        .param("requestId", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/homepage"));

        Mockito.verify(friendService).acceptFriendRequest(10L);
    }

    @Test
    public void testRejectFriendRequest_redirectsHomepage() throws Exception {
        mockMvc.perform(post("/friend/reject")
                        .param("requestId", "10"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/homepage"));

        Mockito.verify(friendService).rejectFriendRequest(10L);
    }

    @Test
    public void testRemoveFriend_redirectsHomepage() throws Exception {
        mockMvc.perform(post("/removeFriend")
                        .param("friendId", "3")
                        .sessionAttr("user", mockUser))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/homepage"));

        Mockito.verify(friendService).endFriendship(1L, 3L);
    }

    @Test
    void testSearchFriends_addsNonFriendsToModel() throws Exception {
        when(friendService.getNonFriendUsers(anyLong()))
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/searchFriends")
                        .sessionAttr("user", mockUser))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("nonFriends"))
                .andExpect(view().name("searchFriends"));
    }

    @Test
    public void testShowAllFriendActs_addsActsToModel() throws Exception {
        when(homepageService.getRecentFriendActivities(anyLong(), any())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/showAllFriendsActs")
                        .sessionAttr("user", mockUser))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("acts"))
                .andExpect(view().name("showAllFriendActs"));
    }
}
