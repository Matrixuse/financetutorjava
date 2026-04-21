package com.financebot.util;

public class PromptBuilder {

    public static String buildEvaluationPrompt(String question, String answer) {

        return "You are a finance tutor.\n"
                + "Evaluate the answer.\n"
                + "Return:\n"
                + "1. Correct/Incorrect\n"
                + "2. Score out of 10\n"
                + "3. Explanation\n\n"
                + "Question: " + question + "\n"
                + "Answer: " + answer;
    }
}