package com.network.buddy.dto;

import com.network.buddy.model.UserEntity;

public record RegisterUserResponse(String username,
        String name,
        String email,
        String id,
        String token) {
    public RegisterUserResponse(UserEntity user, String token) {
        this(user.getName(), user.getUsername(), user.getEmail(), user.getId(), token);
    }
}
