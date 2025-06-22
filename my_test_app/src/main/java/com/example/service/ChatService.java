package com.example.service;

import com.example.model.users.User;
import com.example.model.users.chat.Chat;
import com.example.model.users.chat.Message;
import com.example.repos.ChatRepo;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ChatService {

    private final ChatRepo chatRepo;

    private final UserService userService;

    public ChatService(ChatRepo chatRepo, UserService userService) {
        this.chatRepo = chatRepo;
        this.userService = userService;
    }

    public List<Message> getMessagesByChatId(Long chatId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return chatRepo.findTopByChatIdOrderByDateDesc(chatId, pageable).getContent();
    }

    public List<Message> seeAllMessagesByChatId(Long chatId) {
        Chat chat = chatRepo.findById(chatId).orElse(null);
        if(chat == null) throw new RuntimeException("no chat found");
        return chat.getMessages();
    }

    public void sendMessage(Long chatId, String text,User user) {
        if(user == null) throw new RuntimeException("User not found");
        Chat chat = chatRepo.findById(chatId).orElse(null);
        if (chat == null) throw new RuntimeException("sending Message into chat which is null");
        Message message = new Message(LocalDateTime.now(),text,chat,user);
        chat.addMessage(message);
        message.setChat(chat);
    }

    public Long startChat(@NotNull List<User> users) {
        if (users.size() < 2) throw new RuntimeException("users size is less than 2");

        List<Chat> allChats = chatRepo.findAll();
        for (Chat chat : allChats) {
            List<User> chatUsers = chat.getUsers();
            if (chatUsers.size() != users.size()) {
                continue;
            }
            boolean allMatch = true;
            for (User wanted : users) {
                boolean found = false;
                for (User member : chatUsers) {
                    if (member.getId().equals(wanted.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    allMatch = false;
                    break;
                }
            }
            if (allMatch) {
                return chat.getId();
            }
        }

        Chat chat = new Chat(users);
        Long chatId = chatRepo.save(chat).getId();
        for (User user : users) {
            userService.addUserToChat(chatId,user.getId());
        }
        return chatId;
    }
}
