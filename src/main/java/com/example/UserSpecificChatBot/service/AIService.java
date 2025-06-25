package com.example.UserSpecificChatBot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class AIService {
  //  @Value("${gemini.api.key}")
    private String geminiApiUrl="https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=";
   // @Value("${gemini.api.key}")
    private String geminiApiKey="AIzaSyDj27yFA_YW0S-_8NzbIEyTZO15UhK1xo8";
    private final WebClient webClient;

    public AIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String getAnswer(String question) {
        Map<String, Object> requestBody = Map.of("contents", new Object[]{
                Map.of("parts", new Object[]{ Map.of("text", question) })
        });

        return webClient.post()
                .uri(geminiApiUrl + geminiApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}