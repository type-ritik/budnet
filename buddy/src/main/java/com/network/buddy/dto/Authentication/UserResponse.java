package com.network.buddy.dto.Authentication;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserResponse(
        UUID id, String username, String email) {
}
