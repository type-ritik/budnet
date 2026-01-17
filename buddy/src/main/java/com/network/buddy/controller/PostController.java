package com.network.buddy.controller;

import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.network.buddy.dto.CreatePost.CreatePostRequest;
import com.network.buddy.dto.CreatePost.CreatePostResponse;
import com.network.buddy.dto.ReadPost.ReadPostResponse;
import com.network.buddy.model.ApiResponse;
import com.network.buddy.service.PostService;
import com.network.buddy.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class PostController {

    private final PostService postService;

    public PostController(PostService _postService) {
        this.postService = _postService;
    }

    @PostMapping("/posts")
    public ResponseEntity<ApiResponse<CreatePostResponse>> createPost(@Valid @RequestBody CreatePostRequest request) {

        log.info("Create Post component hit");
        log.info("Request info: " + request.toString());
        var response = postService.makePost(request);
        log.info("Create Post component end");
        return ResponseEntity.ok(ResponseUtil.success(response, "Post created successfully!", "/"));
    }

    @GetMapping("/posts/{slug}/users/{username}")
    public ResponseEntity<ApiResponse<ReadPostResponse>> retrievePostByUsernameSlug(@PathVariable String username,
            @PathVariable String slug) {
        var response = postService.readPostByUsernameSlug(username, slug);
        return ResponseEntity.ok(ResponseUtil.success(response, "Retrieve post successfully", "/"));
    }

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<ReadPostResponse>> retrievePostByPostId(@PathVariable UUID postId) {
        var response = postService.readPostByPostId(postId);
        return ResponseEntity.ok(ResponseUtil.success(response, "Retrieve post successfully", "/"));
    }

    @GetMapping("/posts/users/{userId}")
    public ResponseEntity<ApiResponse<List<ReadPostResponse>>> retrievePostsByUserId(@PathVariable UUID userId,
            @RequestParam(defaultValue = "5") int pageSize) {
        List<ReadPostResponse> response = postService.readPostsByUserId(userId, pageSize);
        return ResponseEntity.ok(ResponseUtil.success(response, "Retrieve list of post successfully", "/"));
    }
}
