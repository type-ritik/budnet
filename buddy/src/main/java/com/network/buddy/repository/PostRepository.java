package com.network.buddy.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.network.buddy.model.PostEntity;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID> {

    PostEntity findByAuthorId(UUID authorId);

    List<PostEntity> findManyByAuthorId(UUID authorId);
}
