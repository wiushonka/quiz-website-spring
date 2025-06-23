package com.example.DTOs;

public class InChatMessageDTO {
    private final Long chatId;
    private final String text;
    private final String sender;

    public InChatMessageDTO(Long chatId, String text, String sender) {
        this.chatId = chatId;
        this.text = text;
        this.sender = sender;
    }

    public Long getChatId() { return chatId; }

    public String getText() { return text; }

    public String getSender() { return sender; }
}
