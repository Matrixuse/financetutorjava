package com.financebot.model;

public class EvaluationResponse {

    private String evaluation;

    public EvaluationResponse() {}

    public EvaluationResponse(String evaluation) {
        this.evaluation = evaluation;
    }

    public String getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(String evaluation) {
        this.evaluation = evaluation;
    }
}