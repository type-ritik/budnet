package com.network.buddy.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.network.buddy.model.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID> {

    PostEntity findByAuthorId(UUID authorId);

    List<PostEntity> findManyByAuthorId(UUID authorId);

    @Query(value = """
            SELECT p.* FROM posts p
            JOIN users u ON p.author_id = u.id
            WHERE u.username = ?1 AND p.title = ?2
            """, nativeQuery = true)
    PostEntity findByAuthorUsernameAndTitle(String username, String slug);
}
