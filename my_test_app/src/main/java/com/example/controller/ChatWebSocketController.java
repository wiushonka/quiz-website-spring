package com.example.controller;

import com.example.DTOs.InChatMessageDTO;
import com.example.DTOs.OutChatMessageDTO;
import com.example.model.users.User;
import com.example.model.users.chat.Chat;
import com.example.model.users.chat.Message;
import com.example.service.ChatService;
import com.example.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Controller
public class ChatWebSocketController {

    private final ChatService chatService;

    private final UserService userService;

    private final SimpMessagingTemplate messagingTemplate;

    public ChatWebSocketController(ChatService chatService, UserService userService,
                                   SimpMessagingTemplate messagingTemplate) {
        this.chatService = chatService;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@NotNull InChatMessageDTO in) {
        Chat chat = chatService.getChatById(in.getChatId());
        User user = userService.getUserByUsername(in.getSender());
        Message message = new Message(LocalDateTime.now(),in.getText(),chat,user);

        chatService.storeMessage(message);

        OutChatMessageDTO out = new OutChatMessageDTO(
                user.getUsername(),
                message.getText(),
                message.getDate().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        messagingTemplate.convertAndSend("/topic/chat/" + chat.getId(), out);
    }
}
