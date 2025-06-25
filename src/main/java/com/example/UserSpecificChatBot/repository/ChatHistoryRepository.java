package com.example.UserSpecificChatBot.repository;

import com.example.UserSpecificChatBot.model.ChatHistory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatHistoryRepository extends MongoRepository<ChatHistory, String> {
    List<ChatHistory> findByUserEmail(String email);
    void deleteByUserEmail(String userEmail);
    //List<ChatHistory> findByUserEmail(String userEmail);
    void deleteByIdAndUserEmail(String id, String userEmail);

}
