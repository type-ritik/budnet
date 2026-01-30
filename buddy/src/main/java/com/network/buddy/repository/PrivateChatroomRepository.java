package com.network.buddy.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.network.buddy.model.PrivateChatRoomEntitiy;

@Repository
public interface PrivateChatroomRepository extends JpaRepository<PrivateChatRoomEntitiy, UUID> {

    PrivateChatRoomEntitiy findBySenderId(UUID senderId);
}
