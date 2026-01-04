package com.network.buddy.dto.Registration;

public record RegisterUserRequest(String name,
                String username,
                String email,
                String password) {
}
