package com.network.buddy.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.network.buddy.dto.Comment.CreateCommentRequest;
import com.network.buddy.dto.Comment.CreateCommentResponse;
import com.network.buddy.dto.ReadComment.ReadCommentResponse;
import com.network.buddy.model.CommentEntity;
import com.network.buddy.repository.CommentRepository;
import com.network.buddy.utils.exception.ResourceNotFoundException;
import com.network.buddy.utils.exception.ResponseNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommentService {

    @Autowired
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository _commentRepository) {
        this.commentRepository = _commentRepository;
    }

    public CreateCommentResponse saveComment(CreateCommentRequest request) {

        if (request.authorId() == null) {
            log.error("Author ID is missing in the comment request");
            throw new ResourceNotFoundException("Author ID cannot be null");
        }

        if (request.postId() == null) {
            log.error("Post ID is missing in the comment request");
            throw new ResourceNotFoundException("Post ID cannot be null");
        }

        if (request.comment().isEmpty()) {
            log.error("Comment content is empty");
            throw new ResourceNotFoundException("Comment content cannot be empty");
        }

        CommentEntity savedComment = new CommentEntity();
        savedComment.setAuthorId(request.authorId());
        savedComment.setComment(request.comment());
        savedComment.setPostId(request.postId());

        if (request.parentCommentId() == null) {
            savedComment.setParentCommentId(request.parentCommentId());
        }

        try {

            CommentEntity createdComment = commentRepository.save(savedComment);
            CreateCommentResponse response = new CreateCommentResponse(createdComment);
            return response;
        } catch (ResponseNotFoundException e) {
            throw new ResponseNotFoundException("Server error");
        } catch (IllegalArgumentException e) {
            throw new ResourceNotFoundException("Invalid comment data provided");
        }
    }

    public List<ReadCommentResponse> retrieveAllCommentsByUserId(UUID userId) {
        if (!(userId.toString().length() < 37 && userId.toString().length() > 35)) {
            log.error("Invalid UUID format for userId: {}", userId);
            throw new ResourceNotFoundException("Invalid UUID format for userId");
        }

        log.info("Hit the service of comments");

        try {
            List<CommentEntity> payload = commentRepository.findManyCommentByAuthorId(userId);

            List<ReadCommentResponse> response = payload.stream().map(ReadCommentResponse::new).toList();
            if (response.isEmpty()) {
                log.error("No comments found for userId: {}", userId);
                throw new ResourceNotFoundException("No comments found for the given userId");
            }
            log.info("Comment retrieval successful");
            return response;
        } catch (IllegalArgumentException e) {
            log.error("Error while retrieving comments: {}", e.getMessage());
            throw new ResponseNotFoundException("Failed to retrieve comments");
        }

    }

    public List<ReadCommentResponse> retrieveAllCommentsByUserIdInPostId(UUID userId, UUID postId) {
        log.info("Hit the Service");
        if (!(userId.toString().length() < 37 && userId.toString().length() > 35)) {
            log.error("Invalid UUID format for userId: {}", userId);
            throw new ResourceNotFoundException("Invalid UUID format for userId");
        }

        if (!(postId.toString().length() < 37 && postId.toString().length() > 35)) {
            log.error("Invalid UUID format for postId: {}", postId);
            throw new ResourceNotFoundException("Invalid UUID format for postId");
        }

        try {
            List<CommentEntity> payload = commentRepository.findManyCommentByAuthorIdAndPostId(userId, postId);

            if (payload.isEmpty()) {
                log.error("No comments found for userId: {} in postId: {}", userId, postId);
                throw new ResourceNotFoundException("No comments found for the given userId in the specified postId");
            }

            List<ReadCommentResponse> responses = payload.stream().map(ReadCommentResponse::new).toList();

            return responses;
        } catch (IllegalArgumentException e) {
            log.error("Error while retrieving comments: {}", e.getMessage());
            throw new ResponseNotFoundException("Failed to retrieve comments");
        }
    }

}
