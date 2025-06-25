package com.example.UserSpecificChatBot.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "chat_history")
public class ChatHistory {
    @Id
    private String id;
    private String userEmail;
    private String question;
    private String answer;
    private LocalDateTime timestamp;
    // Getters & Setters
}