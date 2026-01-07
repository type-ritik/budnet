package com.network.buddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.network.buddy.dto.Comment.CreateCommentRequest;
import com.network.buddy.dto.Comment.CreateCommentResponse;
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

}
