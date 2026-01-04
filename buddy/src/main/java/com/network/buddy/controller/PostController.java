package com.network.buddy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.network.buddy.dto.CreatePost.CreatePostRequest;
import com.network.buddy.service.PostService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/public/v1")
public class PostController {

    private final PostService postService;

    public PostController(PostService _postService) {
        this.postService = _postService;
    }

    @PostMapping("/create-post")
    public ResponseEntity<?> createPost(@Valid @RequestBody CreatePostRequest request) {
        try {
            log.info("Create Post component hit");
            log.info("Request info: " + request.toString());
            var response = postService.makePost(request);
            log.info("Create Post component end");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getLocalizedMessage());
        }
    }
}
