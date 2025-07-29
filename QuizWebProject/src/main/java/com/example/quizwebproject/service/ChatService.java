package com.example.quizwebproject.service;

import com.example.quizwebproject.model.users.User;
import com.example.quizwebproject.model.users.chat.Chat;
import com.example.quizwebproject.model.users.chat.Message;
import com.example.quizwebproject.repos.ChatRepo;
import com.example.quizwebproject.repos.MessagesRepo;
import com.example.quizwebproject.service.UserService;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ChatService {

    private final ChatRepo chatRepo;

    private final UserService userService;

    private final MessagesRepo messagesRepo;

    public ChatService(ChatRepo chatRepo, UserService userService, MessagesRepo messagesRepo) {
        this.chatRepo = chatRepo;
        this.userService = userService;
        this.messagesRepo = messagesRepo;
    }

    public List<Message> getMessagesByChatId(Long chatId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return messagesRepo.findTopByChatIdOrderByDateAsc(chatId, pageable).getContent();
    }

    public List<Message> seeAllMessagesByChatId(Long chatId) {
        Chat chat = chatRepo.findById(chatId).orElse(null);
        if(chat == null) throw new RuntimeException("no chat found");
        return chat.getMessages();
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

    public Chat getChatById(Long chatId) {
        return chatRepo.findById(chatId).orElseThrow(()->new RuntimeException("no chat found"));
    }

    public void storeMessage(Message message) {
        messagesRepo.save(message);
    }
}
