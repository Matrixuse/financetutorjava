package com.financebot.controller;

import com.financebot.service.GroqService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")

@RestController
@RequestMapping("/api/quiz")
public class QuizController {

    private final GroqService groqService;

    public QuizController(GroqService groqService) {
        this.groqService = groqService;
    }


    @GetMapping("/question")
    public Map<String, String> getQuestion(@RequestParam String difficulty) {
        String q = groqService.generateQuestion(difficulty);
        return Map.of("question", q);
    }

    @PostMapping("/evaluate")
    public Map<String, String> evaluate(@RequestBody Map<String, String> req) {
        String evaluation = groqService.evaluateAnswer(
            req.get("question"),
            req.get("answer")
        );
        
        // Parse the evaluation response to extract score and feedback
        String score = "N/A";
        String feedback = evaluation;
        
        if (evaluation.contains("Score:")) {
            String[] parts = evaluation.split("Feedback:");
            if (parts.length > 0) {
                String scorePart = parts[0].replace("Score:", "").trim();
                score = scorePart.split("\n")[0].trim();
            }
            if (parts.length > 1) {
                feedback = parts[1].trim();
            }
        }
        
        return Map.of(
            "score", score,
            "feedback", feedback,
            "evaluation", evaluation
        );
    }

    @GetMapping("/explain")
    public Map<String, String> explain(@RequestParam String topic) {
        String res = groqService.explainTopic(topic);
        return Map.of("explanation", res);
    }

    @PostMapping("/submit")
    public Map<String, String> submitAnswer(@RequestBody Map<String, String> request) {

        String question = request.get("question");
        String answer = request.get("answer");

        String evaluation = groqService.evaluateAnswer(question, answer);

        return Map.of("evaluation", evaluation);
    }
}
