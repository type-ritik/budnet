package com.network.buddy.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.network.buddy.model.CommentEntity;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, UUID> {
    List<CommentEntity> findByAuthorId(UUID authorId);

    List<CommentEntity> findByPostId(UUID postId);

    boolean existsById(UUID id);

    @Query(value = """
            SELECT c.id, p.title, c.post_id, c.author_id, c.parent_comment_id, c.comment, c.commented_at FROM comments c JOIN posts p ON c.post_id = p.id WHERE c.author_id = ?1
            """, nativeQuery = true)
    List<CommentEntity> findManyCommentByAuthorId(UUID authorId);

    @Query(value = """
            SELECT c.id, p.title, c.post_id, c.author_id, c.parent_comment_id, c.comment, c.commented_at FROM comments c JOIN posts p ON c.post_id = p.id WHERE c.author_id = ?1 AND c.post_id = ?2
            """, nativeQuery = true)
    List<CommentEntity> findManyCommentByAuthorIdAndPostId(UUID authorId, UUID postId);

}
