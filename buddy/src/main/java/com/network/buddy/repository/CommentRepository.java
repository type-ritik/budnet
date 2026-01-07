package com.network.buddy.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.network.buddy.model.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {
    List<CommentEntity> findByAuthorId(UUID authorId);

    List<CommentEntity> findByPostId(UUID postId);

    boolean existsById(UUID id);

}
