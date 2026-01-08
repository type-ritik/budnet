package com.network.buddy.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.network.buddy.dto.Comment.CreateCommentRequest;
import com.network.buddy.dto.Comment.CreateCommentResponse;
import com.network.buddy.dto.ReadComment.ReadCommentResponse;
import com.network.buddy.model.ApiResponse;
import com.network.buddy.service.CommentService;
import com.network.buddy.utils.ResponseUtil;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/public/v1/post/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService _commentService) {
        this.commentService = _commentService;
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<CreateCommentResponse>> createComment(
            @Valid @RequestBody CreateCommentRequest request) {
        log.info("Create comment API hit");
        CreateCommentResponse response = commentService.saveComment(request);
        log.info("Create comment API end");
        return ResponseEntity.ok(ResponseUtil.success(response, "Comment created successfully", "/"));

    }

    @GetMapping("/by/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReadCommentResponse>>> allCommentsByUserId(@PathVariable UUID userId) {
        log.info("Successfully retrieved userId");
        List<ReadCommentResponse> response = commentService.retrieveAllCommentsByUserId(userId);
        log.info("All comments by userId retrieved successfully");
        return ResponseEntity.ok(ResponseUtil.success(response, "Comments retrieve Successfully", "/"));

    }

    @GetMapping("by/user-post/{userId}/{postId}")
    public ResponseEntity<ApiResponse<List<ReadCommentResponse>>> allCommentsByUserIdInPostId(@PathVariable UUID userId,
            @PathVariable UUID postId) {
        log.info("Successfully retrieved userId and postId");
        List<ReadCommentResponse> response = commentService.retrieveAllCommentsByUserIdInPostId(userId, postId);

        return ResponseEntity.ok(ResponseUtil.success(response, "Successfully retrieve Comments", "/"));
    }
}
