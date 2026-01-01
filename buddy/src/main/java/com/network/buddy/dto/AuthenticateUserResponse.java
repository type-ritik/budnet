package com.network.buddy.dto;

import com.network.buddy.model.UserEntity;

public record AuthenticateUserResponse(
        String id,
        String username,
        String name,
        String email,
        String token) {
    public AuthenticateUserResponse(UserEntity user, String token) {
        this(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail(), token);
    }
}
