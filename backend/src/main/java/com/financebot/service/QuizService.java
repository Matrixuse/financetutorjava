package com.financebot.service;

import com.financebot.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class QuizService {

    @Autowired
    private GroqService groqService;

    public Question getQuestion() {
        return new Question("What is diversification in investment?");
    }

    public EvaluationResponse evaluateAnswer(AnswerRequest request) {

        String result = groqService.evaluateAnswer(
                request.getQuestion(),
                request.getAnswer()
        );

        return new EvaluationResponse(result);
    }
}