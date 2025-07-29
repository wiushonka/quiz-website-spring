package com.example.quizwebproject.DTOs;

public class OutChatDTO {
    private final String sender;
    private final String text;
    private final long timestamp;

    public OutChatDTO(String sender, String text, long timestamp) {
        this.sender = sender;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String getSender()    { return sender; }

    public String getText()      { return text; }

    public long getTimestamp() { return timestamp; }
}
