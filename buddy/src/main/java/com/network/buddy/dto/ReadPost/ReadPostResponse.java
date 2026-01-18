package com.network.buddy.dto.ReadPost;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import com.network.buddy.model.PostEntity;

public record ReadPostResponse(UUID id, UUID authorId, String title, String content, Date writtenAt) {
    public ReadPostResponse(PostEntity post) {
        this(post.getId(), post.getAuthorId(), post.getTitle(), post.getContent(), post.getWrittenAt());
    }

    public ReadPostResponse(Optional<PostEntity> post) {
        this(post.orElseThrow().getId(),
                post.orElseThrow().getAuthorId(),
                post.orElseThrow().getTitle(),
                post.orElseThrow().getContent(),
                post.orElseThrow().getWrittenAt());
    }

}
