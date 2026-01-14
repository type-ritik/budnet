package com.network.buddy.dto.Registration;

import java.util.UUID;

import com.network.buddy.model.UserEntity;

public record RegisterUserResponse(String username,
        String name,
        String email,
        UUID id,
        String token) {
    public RegisterUserResponse(UserEntity user, String token) {
        this(user.getName(), user.getUsername(), user.getEmail(), user.getId(), token);
    }

    public RegisterUserResponse(String name, String email, String id, String password, String username, String token) {
        this(name, email, username, UUID.fromString(id), token);
    }
}
