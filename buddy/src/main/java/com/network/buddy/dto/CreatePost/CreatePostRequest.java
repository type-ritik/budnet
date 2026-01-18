package com.network.buddy.dto.CreatePost;

import java.util.UUID;

public record CreatePostRequest(String title,
                String content,
                UUID authorId) {
}
