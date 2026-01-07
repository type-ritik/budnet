package com.network.buddy.model;

import java.util.Date;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.network.buddy.dto.Comment.CreateCommentRequest;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, name = "author_id")
    private UUID authorId;

    @Column(nullable = false, name = "post_id")
    private UUID postId;

    @Column(name = "parent_comment_id")
    private UUID parentCommentId;

    @Column(nullable = false, name = "comment", columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(nullable = false, name = "commented_at")
    private Date commentedAt;

    // Constructor
    public CommentEntity() {
    }

    public CommentEntity(CreateCommentRequest request) {
        this.authorId = request.authorId();
        this.postId = request.postId();
        this.comment = request.comment();
    }

    // Setters

    public void setAuthorId(UUID _authorId) {
        this.authorId = _authorId;
    }

    public void setPostId(UUID _postId) {
        this.postId = _postId;
    }

    public void setParentCommentId(UUID _parentCommentId) {
        this.parentCommentId = _parentCommentId;
    }

    public void setComment(String _comment) {
        this.comment = _comment;
    }

    public void setCommentedAt(Date _commentedAt) {
        this.commentedAt = _commentedAt;
    }

    // Getters
    public UUID getId() {
        return id;
    }

    public UUID getAuthorId() {
        return authorId;
    }

    public UUID getPostId() {
        return postId;
    }

    public UUID getParentCommentId() {
        return parentCommentId;
    }

    public String getComment() {
        return comment;
    }

    public Date getCommentedAt() {
        return commentedAt;
    }
}
