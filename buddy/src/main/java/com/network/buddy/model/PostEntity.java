package com.network.buddy.model;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import com.network.buddy.dto.CreatePost.CreatePostResponse;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, name = "author_id")
    private UUID authorId;

    @Column(nullable = false, length = 80)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(nullable = false, name = "written_at")
    private Date writtenAt;

    @OneToMany(mappedBy = "posts")
    private List<CommentEntity> comments;

    public PostEntity(CreatePostResponse post) {
        this.title = post.title();
        this.content = post.content();
        this.authorId = post.authorId();
    }

    public PostEntity() {
    }

    public void setTitle(String _title) {
        this.title = _title;
    }

    public void setId(UUID _id) {
        this.id = _id;
    }

    public void setContent(String _content) {
        this.content = _content;
    }

    public void setAuthorId(UUID _authorId) {
        this.authorId = _authorId;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public UUID getAuthorId() {
        return this.authorId;
    }

    public UUID getId() {
        return this.id;
    }

    public Date getWrittenAt() {
        return this.writtenAt;
    }

}
