package com.example.UserSpecificChatBot.service;

import com.example.UserSpecificChatBot.repository.ChatHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatHistoryService {

    @Autowired
    private ChatHistoryRepository chatHistoryRepository;

    public void deleteAllByUser(String email) {
        chatHistoryRepository.deleteByUserEmail(email);
    }

    public void deleteByIdAndUser(String id, String email) {
        chatHistoryRepository.deleteByIdAndUserEmail(id, email);
    }

}
