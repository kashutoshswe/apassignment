package com.assignment.apassignment.models;

public class JwtResponse {
    private String token;

    public JwtResponse(String newToken) {
        token = newToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
