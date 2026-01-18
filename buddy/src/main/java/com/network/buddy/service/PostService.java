package com.network.buddy.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.network.buddy.dto.CreatePost.CreatePostRequest;
import com.network.buddy.dto.CreatePost.CreatePostResponse;
import com.network.buddy.dto.ReadPost.ReadPostResponse;
import com.network.buddy.model.PostEntity;
import com.network.buddy.model.UserEntity;
import com.network.buddy.repository.PostRepository;
import com.network.buddy.repository.UserRepository;
import com.network.buddy.utils.exception.ResourceNotFoundException;
import com.network.buddy.utils.exception.ResponseNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    @Autowired
    private final UserRepository userRepository;

    public PostService(PostRepository _postRepository, UserRepository _userRepository) {
        this.postRepository = _postRepository;
        this.userRepository = _userRepository;
    }

    public CreatePostResponse makePost(CreatePostRequest post) {
        log.info("Creating a new post");

        // business rule here...
        if (post.title().isEmpty()) {
            throw new ResourceNotFoundException("Post title is required");
        }
        log.info("Valid post title");

        if (post.content().isEmpty()) {
            throw new ResourceNotFoundException("Post content is required");
        }

        log.info("Valid post content");

        UserEntity authorProxy = userRepository.getReferenceById(post.authorId());

        PostEntity newPost = new PostEntity();
        newPost.setAuthors(authorProxy);
        newPost.setContent(post.content());
        newPost.setTitle(post.title());

        log.info("Entity is created: " + newPost.toString());

        // Additional business rules can be added here
        try {
            PostEntity savedPost = postRepository.save(newPost);
            CreatePostResponse response = new CreatePostResponse(savedPost);
            log.info("PostCreated: " + response.toString());
            log.info("Post created successfully");
            return response;
        } catch (ResponseNotFoundException e) {
            log.error("Error while creating post: " + e.getMessage());
            throw new ResponseNotFoundException("Failed to create post");
        }
    }

    public ReadPostResponse readPostByUsernameSlug(String username, String slug) {
        if (username == null) {
            throw new ResourceNotFoundException("Username is required");
        }

        if (slug == null) {
            throw new ResourceNotFoundException("Slug is required");
        }

        if (slug.length() < 4) {
            throw new ResourceNotFoundException("Slug is too short");
        }

        if (username.length() < 4) {
            throw new ResourceNotFoundException("Username is too short");
        }

        try {
            PostEntity post = postRepository.findByAuthorUsernameAndTitle(username, slug);

            ReadPostResponse response = new ReadPostResponse(post);

            return response;
        } catch (IllegalArgumentException e) {
            log.error("Error while retrieving post: " + e.getMessage());
            throw new ResponseNotFoundException("Failed to retrieve post");
        }
    }

    public ReadPostResponse readPostByPostId(UUID postId) {
        if (postId == null) {
            throw new ResourceNotFoundException("Post ID is required");
        }

        if (postId.toString().length() < 36) {
            throw new ResourceNotFoundException("Post ID is invalid");
        }

        try {
            Optional<PostEntity> post = postRepository.findById(postId);

            ReadPostResponse response = new ReadPostResponse(post);

            return response;
        } catch (IllegalArgumentException e) {
            log.error("Error while retrieving post: " + e.getMessage());
            throw new ResponseNotFoundException("Failed to retrieve post");
        }
    }

    public List<ReadPostResponse> readPostsByUserId(UUID userId, int pageSize) {
        if (userId == null) {
            throw new ResourceNotFoundException("Post ID is required");
        }

        if (userId.toString().length() < 36) {
            throw new ResourceNotFoundException("Post ID is invalid");
        }

        try {
            List<PostEntity> posts = postRepository.findByAuthorId(userId);

            List<ReadPostResponse> response = posts.stream()
                    .limit(pageSize)
                    .map(ReadPostResponse::new)
                    .toList();

            return response;
        } catch (IllegalArgumentException e) {
            log.error("Error while retrieving posts: " + e.getMessage());
            throw new ResponseNotFoundException("Failed to retrieve posts");
        }
    }
}
