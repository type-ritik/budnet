package com.network.buddy.dto.Comment;

import java.util.Date;
import java.util.UUID;

import com.network.buddy.model.CommentEntity;

public record CreateCommentResponse(UUID id, UUID authorId, UUID postId, UUID parentCommentId, String comment,
        Date commentedAt) {
    public CreateCommentResponse(CommentEntity comment) {
        this(comment.getId(), comment.getAuthorId(), comment.getPostId(), comment.getParentCommentId(),
                comment.getComment(), comment.getCommentedAt());
    }

}
