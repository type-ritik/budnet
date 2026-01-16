package com.network.buddy.model;

import java.util.Date;
import java.util.UUID;
import org.hibernate.annotations.CreationTimestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(name = "post_id", insertable = false, updatable = false)
    private UUID postId;

    @Column(name = "author_id", insertable = false, updatable = false)
    private UUID authorId;

    @Column(name = "parent_comment_id", insertable = false, updatable = false)
    private UUID parentCommentId;

    @Column(nullable = false, name = "comment", columnDefinition = "TEXT")
    private String comment;

    @CreationTimestamp
    @Column(nullable = false, name = "commented_at")
    private Date commentedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private PostEntity posts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserEntity authors;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private CommentEntity parentComments;

    // Constructor
    public CommentEntity() {
    }

    public CommentEntity(String comment, PostEntity post, UserEntity user, CommentEntity _parentComment) {
        this.comment = comment;
        this.posts = post;
        this.authors = user;
        this.parentComments = _parentComment;
    }

    // Setters
    public void setId(UUID _id) {
        this.id = _id;
    }

    public void setAuthor(UserEntity _author) {
        this.authors = _author;
    }

    public void setPost(PostEntity _post) {
        this.posts = _post;
    }

    public void setParentComments(CommentEntity _parentComment) {
        this.parentComments = _parentComment;
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
        return (authors != null) ? authors.getId() : null;
    }

    public UUID getPostId() {
        return (posts != null) ? posts.getId() : null;
    }

    public UUID getParentCommentId() {
        return (parentComments != null) ? parentComments.getId() : null;
    }

    public String getComment() {
        return comment;
    }

    public Date getCommentedAt() {
        return commentedAt;
    }

    public PostEntity getPosts() {
        return posts;
    }

    public UserEntity getAuthors() {
        return authors;
    }

    public CommentEntity getParentComments() {
        return parentComments;
    }

}
