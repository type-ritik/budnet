package com.network.buddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.network.buddy.dto.CreatePost.CreatePostRequest;
import com.network.buddy.dto.CreatePost.CreatePostResponse;
import com.network.buddy.model.PostEntity;
import com.network.buddy.repository.PostRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    public PostService(PostRepository _postRepository) {
        this.postRepository = _postRepository;
    }

    public CreatePostResponse makePost(CreatePostRequest post) {
        log.info("Creating a new post");

        // business rule here...
        if (post.title() == null || post.title().isBlank()) {
            throw new IllegalArgumentException("Post title is required");
        }
        log.info("Valid post title");

        if (post.content() == null) {
            throw new IllegalArgumentException("Post content is required");
        }

        log.info("Valid post content");

        PostEntity newPost = new PostEntity();
        newPost.setAuthorId(post.authorId());
        newPost.setContent(post.content());
        newPost.setTitle(post.title());

        log.info("Entity is created: " + newPost.toString());

        // Additional business rules can be added here
        PostEntity savedPost = postRepository.save(newPost);
        CreatePostResponse response = new CreatePostResponse(savedPost);

        log.info("PostCreated: " + response.toString());
        log.info("Post created successfully");
        return response;
    }
}
