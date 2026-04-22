package com.financebot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.Duration;
import java.util.*;

@Service
public class GroqService {

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    private final WebClient webClient;

    public GroqService(WebClient webClient) {
        this.webClient = webClient;
    }

    // ✅ EVALUATE ANSWER
    public String evaluateAnswer(String question, String answer) {
    try {
        Map<String, Object> body = new HashMap<>();

        body.put("model", "llama-3.1-8b-instant");

        List<Map<String, String>> messages = new ArrayList<>();

        messages.add(Map.of(
            "role", "system",
            "content", "You are a strict finance examiner."
        ));

        messages.add(Map.of(
            "role", "user",
            "content",
            "Question: " + question +
            "\nAnswer: " + answer +
            "\nProvide feedback in this format:\nScore: X/10\nFeedback: [Detailed explanation of what was right/wrong with the answer]"
        ));

        body.put("messages", messages);
        body.put("temperature", 0.3);
        body.put("max_tokens", 150);

        String response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block(Duration.ofSeconds(10));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response);

        return json.get("choices").get(0).get("message").get("content").asText();

        } catch (Exception e) {
             return "Error evaluating answer";
        }
    }

    // ✅ GENERATE QUESTION (AI)
    public String generateQuestion(String difficulty) {
    try {

        Map<String, Object> body = new HashMap<>();

        body.put("model", "llama-3.1-8b-instant");

        List<Map<String, String>> messages = new ArrayList<>();

        messages.add(Map.of(
            "role", "system",
            "content", "You are a finance tutor."
        ));

        messages.add(Map.of(
            "role", "user",
            "content",
            "Generate one " + difficulty +
            " level finance question. Only question, no explanation."
        ));

        body.put("messages", messages);

        String response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block(Duration.ofSeconds(10));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response);

        return json.get("choices").get(0).get("message").get("content").asText();

        } catch (Exception e) {
            return "Error generating question";
        }
    }

//     extra topics
    public String explainTopic(String topic) {
    try {

        Map<String, Object> body = new HashMap<>();

        body.put("model", "llama-3.1-8b-instant");

        List<Map<String, String>> messages = new ArrayList<>();

        messages.add(Map.of(
            "role", "system",
            "content", "You are a finance tutor."
        ));

        messages.add(Map.of(
            "role", "user",
            "content",
            "Explain this finance concept in simple terms: " + topic
        ));

        body.put("messages", messages);

        String response = webClient.post()
                .uri(apiUrl)
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .block(Duration.ofSeconds(10));

        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response);

        return json.get("choices").get(0).get("message").get("content").asText();

        } catch (Exception e) {
             return "Error explaining topic";
        }
    }
}