package com.assignment.apassignment.models;

public class RefreshRequest {
    private String refreshToken;
    public RefreshRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}

