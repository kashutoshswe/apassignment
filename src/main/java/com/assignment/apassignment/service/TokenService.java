package com.assignment.apassignment.service;

import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class TokenService {
    private final Set<String> revokedTokens = new HashSet<>();

    public void revokeToken(String token) {
        revokedTokens.add(token);
    }

    public boolean isTokenRevoked(String token) {
        return revokedTokens.contains(token);
    }
}

