package com.assignment.apassignment.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class JwtResponse {
    private String token;

    public JwtResponse(String newToken) {
        token = newToken;
    }
}
