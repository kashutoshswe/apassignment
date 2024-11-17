package com.assignment.apassignment.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // Add roles, email, or other fields if needed
    public User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}

