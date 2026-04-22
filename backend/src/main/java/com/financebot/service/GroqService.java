package com.financebot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.*;

@Service
public class GroqService {

    private static final Logger logger = LoggerFactory.getLogger(GroqService.class);

    @Value("${groq.api.key}")
    private String apiKey;

    @Value("${groq.api.url}")
    private String apiUrl;

    private final WebClient webClient;

    public GroqService(WebClient webClient) {
        this.webClient = webClient;
    }

    private String extractResponseContent(String response) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(response);
        
        if (json == null || !json.has("choices")) {
            throw new Exception("Invalid response: missing 'choices' field");
        }
        
        JsonNode choices = json.get("choices");
        if (choices == null || !choices.isArray() || choices.size() == 0) {
            throw new Exception("Invalid response: 'choices' array is empty");
        }
        
        JsonNode choice = choices.get(0);
        if (choice == null || !choice.has("message")) {
            throw new Exception("Invalid response: missing 'message' in choice");
        }
        
        JsonNode message = choice.get("message");
        if (message == null || !message.has("content")) {
            throw new Exception("Invalid response: missing 'content' in message");
        }
        
        return message.get("content").asText();
    }

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
                    .block(Duration.ofSeconds(20));

            return extractResponseContent(response);

        } catch (java.util.concurrent.TimeoutException e) {
            logger.error("Timeout evaluating answer - Groq API took longer than 20 seconds", e);
            return "Error: Groq API timeout. Please try again.";
        } catch (Exception e) {
            logger.error("Error evaluating answer", e);
            return "Error evaluating answer: " + e.getMessage();
        }
    }

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

            logger.info("Calling Groq API to generate {} level question", difficulty);
            
            String response = webClient.post()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block(Duration.ofSeconds(20));

            logger.info("Groq API responded successfully");
            return extractResponseContent(response);

        } catch (java.util.concurrent.TimeoutException e) {
            logger.error("Timeout generating question - Groq API took longer than 20 seconds", e);
            return "Error: Groq API timeout. Please try again.";
        } catch (Exception e) {
            logger.error("Error generating question", e);
            return "Error generating question: " + e.getMessage();
        }
    }

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
                    .block(Duration.ofSeconds(20));

            return extractResponseContent(response);

        } catch (java.util.concurrent.TimeoutException e) {
            logger.error("Timeout explaining topic - Groq API took longer than 20 seconds", e);
            return "Error: Groq API timeout. Please try again.";
        } catch (Exception e) {
            logger.error("Error explaining topic", e);
            return "Error explaining topic: " + e.getMessage();
        }
    }
}