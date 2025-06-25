package com.example.UserSpecificChatBot.controller;

import com.example.UserSpecificChatBot.dto.ChatRequest;
import com.example.UserSpecificChatBot.model.ChatHistory;
import com.example.UserSpecificChatBot.model.User;
import com.example.UserSpecificChatBot.repository.ChatHistoryRepository;
import com.example.UserSpecificChatBot.service.AIService;
import com.example.UserSpecificChatBot.service.AuthService;
import com.example.UserSpecificChatBot.service.ChatHistoryService;
import com.example.UserSpecificChatBot.token.JWTUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin
public class ChatController {
    @Autowired
    private AIService aiService;
    @Autowired private AuthService authService;
    @Autowired private ChatHistoryRepository chatRepo;

    @PostMapping("/ask")
    public ResponseEntity<String> ask(@RequestHeader("Authorization") String authHeader,
                                      @RequestBody ChatRequest req) {
        String token = authHeader.substring(7);
        User currentUser = authService.getCurrentUser(token);
        String response = aiService.getAnswer(req.getQuestion());

        String answerText = extractAnswer(response);

        ChatHistory chat = new ChatHistory();
        chat.setUserEmail(currentUser.getEmail());
        chat.setQuestion(req.getQuestion());
        chat.setAnswer(answerText);
        chat.setTimestamp(LocalDateTime.now());
        chatRepo.save(chat);

        return ResponseEntity.ok(answerText);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ChatHistory>> getHistory(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        User user = authService.getCurrentUser(token);
        return ResponseEntity.ok(chatRepo.findByUserEmail(user.getEmail()));
    }

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private ChatHistoryService chatHistoryService;
    @DeleteMapping("/history")
    public ResponseEntity<?> deleteHistory(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7); // remove "Bearer "
        String email = jwtUtil.extractEmail(token);
        chatHistoryService.deleteAllByUser(email);
        return ResponseEntity.ok("Chat history deleted successfully");
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<?> deleteChatById(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable String id) {
        String token = authHeader.substring(7); // remove Bearer
        String email = jwtUtil.extractEmail(token);
        chatHistoryService.deleteByIdAndUser(id,email);
        return ResponseEntity.ok("Chat message deleted successfully");
    }



    //response for plain text form gemini
    private String extractAnswer(String response) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(response);
            return root.path("candidates").get(0)
                    .path("content").path("parts").get(0).path("text").asText();
        } catch (Exception e) {
            return "Unable to parse response.";
        }
    }
}