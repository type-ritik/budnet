package com.network.buddy.dto;

public record RegisterUserRequest(String name,
        String username,
        String email,
        String password) {
}
