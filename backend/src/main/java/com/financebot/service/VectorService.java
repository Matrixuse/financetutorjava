package com.financebot.service;

import org.springframework.stereotype.Service;

@Service
public class VectorService {

    public String getContext(String query) {
        return "Finance context for: " + query;
    }
}