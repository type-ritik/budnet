package com.network.buddy.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.network.buddy.dto.CreatePost.CreatePostRequest;
import com.network.buddy.dto.CreatePost.CreatePostResponse;
import com.network.buddy.model.ApiResponse;
import com.network.buddy.service.PostService;
import com.network.buddy.utils.ResponseUtil;
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
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(@Valid @RequestBody CreatePostRequest request) {

        log.info("Create Post component hit");
        log.info("Request info: " + request.toString());
        var response = postService.makePost(request);
        log.info("Create Post component end");
        return ResponseEntity.ok(ResponseUtil.success(response, "Post created successfully!", "/"));
    }
}
