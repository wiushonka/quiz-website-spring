package com.example.controller;

import com.example.model.users.User;
import com.example.model.users.chat.Message;
import com.example.service.ChatService;
import com.example.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ChatController {
    private final ChatService chatService;

    private final UserService userService;

    public ChatController(ChatService chatService, UserService userService) {
        this.chatService = chatService;
        this.userService = userService;
    }

    @GetMapping("/{chatId}")
    public String openChat(@PathVariable Long chatId, Model model) {
        List<Message> messages = chatService.getMessagesByChatId(chatId, 0, 20);
        model.addAttribute("chatId", chatId);
        model.addAttribute("messages", messages);
        return "chat";
    }

    @GetMapping("/{chatId}/messages")
    @ResponseBody
    public List<Message> loadMoreMessages(@PathVariable Long chatId,
                                          @RequestParam int page,
                                          @RequestParam(defaultValue = "20") int size) {
        return chatService.getMessagesByChatId(chatId, page, size);
    }

    @PostMapping("/chat/{chatId}/send")
    public String sendMessage(@PathVariable Long chatId,
                              @RequestParam("text") String text,
                              HttpSession session) {
        User user = (User) session.getAttribute("user");
        chatService.sendMessage(chatId,text,user);
        return "redirect:/" + chatId;
    }

    @GetMapping("/chatWithOneUser")
    public String createChatWithOneUser(HttpSession session,
                                        @RequestParam("clickedUserId") Long clickedUserId) {
        User first = (User) session.getAttribute("user");
        User second = userService.getUserById(clickedUserId);
        List<User> users = new ArrayList<>();
        users.add(first);
        users.add(second);
        Long chatId = chatService.startChat(users);
        return "redirect:/" + chatId;
    }
}
