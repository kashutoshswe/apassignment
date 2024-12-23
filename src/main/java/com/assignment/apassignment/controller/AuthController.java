package com.assignment.apassignment.controller;

import com.assignment.apassignment.entity.User;
import com.assignment.apassignment.models.JwtResponse;
import com.assignment.apassignment.models.LoginRequest;
import com.assignment.apassignment.models.RefreshRequest;
import com.assignment.apassignment.models.SignupRequest;
import com.assignment.apassignment.repository.UserRepository;
import com.assignment.apassignment.service.TokenService;
import com.assignment.apassignment.utility.JwtUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth APIs", description = "Auth management APIs")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody SignupRequest request)
    {
        if (userRepository.existsByUsername(request.getUsername()))
        {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        User user = new User(request.getUsername(), new BCryptPasswordEncoder().encode(request.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok("User created successfully");
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody LoginRequest request)
    {
        try
        {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            String token = jwtUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(new JwtResponse(token));
        }
        catch (Exception e)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest request)
    {
        if (!jwtUtil.isTokenExpired(request.getRefreshToken()))
        {
            String username = jwtUtil.extractUsername(request.getRefreshToken());
            String newToken = jwtUtil.generateToken(username);
            return ResponseEntity.ok(new JwtResponse(newToken));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh token expired");
    }

    @PostMapping("/revoke")
    public ResponseEntity<String> revokeToken(@RequestHeader("Authorization") String token) {
        String tokenStr = token.replace("Bearer ", "");
        tokenService.revokeToken(tokenStr);
        return ResponseEntity.ok("Token successfully revoked");
    }
}

