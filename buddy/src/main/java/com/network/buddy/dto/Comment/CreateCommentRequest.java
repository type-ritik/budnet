package com.network.buddy.dto.Comment;

import java.util.UUID;

public record CreateCommentRequest(UUID authorId, UUID postId, String comment, UUID parentCommentId) {
}
