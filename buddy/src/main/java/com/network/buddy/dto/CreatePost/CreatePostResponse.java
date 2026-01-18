package com.network.buddy.dto.CreatePost;

import java.util.Date;
import java.util.UUID;

import com.network.buddy.model.PostEntity;

public record CreatePostResponse(String title, String content, UUID authorId, UUID id, Date writtenAt) {
    public CreatePostResponse(PostEntity post) {
        this(post.getTitle(), post.getContent(), post.getAuthorId(), post.getId(), post.getWrittenAt());
    }

}
