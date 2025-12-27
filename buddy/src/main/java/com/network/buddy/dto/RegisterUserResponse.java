package com.network.buddy.dto;

import com.network.buddy.model.UserEntity;

public record RegisterUserResponse(String username,
        String name,
        String email,
        Long id) {
    public RegisterUserResponse(UserEntity user) {
        this(user.getName(), user.getUsername(), user.getEmail(), user.getId());
    }
}
