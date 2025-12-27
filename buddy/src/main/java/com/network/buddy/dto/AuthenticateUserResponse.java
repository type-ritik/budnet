package com.network.buddy.dto;

import com.network.buddy.model.UserEntity;

public record AuthenticateUserResponse(
        Long id,
        String username,
        String name,
        String email) {
    public AuthenticateUserResponse(UserEntity user) {
        this(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getEmail());
    }
}
