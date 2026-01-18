package com.network.buddy.model;

import java.sql.Date;
import java.util.List;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(nullable = false, name = "author_id", insertable = false, updatable = false)
    private UUID authorId;

    @Column(nullable = false, length = 80)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreationTimestamp
    @Column(nullable = false, name = "written_at")
    private Date writtenAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private UserEntity authors;

    @OneToMany(mappedBy = "posts")
    private List<CommentEntity> comments;

    public PostEntity(String _title, UserEntity _authors, String _content) {
        this.title = _title;
        this.content = _content;
        this.authors = _authors;
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

    public void setAuthors(UserEntity _authors) {
        this.authors = _authors;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public UUID getAuthorId() {
        return (authors != null) ? authors.getId() : null;
    }

    public UUID getId() {
        return this.id;
    }

    public Date getWrittenAt() {
        return this.writtenAt;
    }

}
