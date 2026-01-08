package com.network.buddy.dto.ReadComment;

import java.util.UUID;

import com.network.buddy.model.CommentEntity;

public record ReadCommentResponse(
        UUID id,
        String title,
        UUID authorId,
        UUID postId,
        UUID parentCommentId,
        String comment,
        String commentedAt) {

    public ReadCommentResponse(CommentEntity comment) {
        this(
                comment.getId(),
                comment.getPostTitle(),
                comment.getPostId(),
                comment.getAuthorId(),
                comment.getParentCommentId(),
                comment.getComment(),
                comment.getCommentedAt().toString());
    }

}
