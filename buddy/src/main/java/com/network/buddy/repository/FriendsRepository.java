package com.network.buddy.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.network.buddy.model.FriendsEntity;

@Repository
public interface FriendsRepository extends JpaRepository<FriendsEntity, UUID> {

    FriendsEntity findFriendsById(UUID id);

    FriendsEntity findByRequestId(UUID requestId);

    // Followers and not deleted
    List<FriendsEntity> findByFriendBIdAndIsFriendshipDeletedIsFalse(UUID friendBId);

    // Following and Not deleted
    List<FriendsEntity> findByFriendAIdAndIsFriendshipDeletedIsFalse(UUID friendAId);
}
