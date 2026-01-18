package com.network.buddy.repository;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.network.buddy.model.RequestState;
import com.network.buddy.model.RequestsEntity;

@Repository
public interface RequestsRepository extends JpaRepository<RequestsEntity, UUID> {
    RequestsEntity findRequestsById(UUID id);

    boolean existsBySenderIdAndReceiverId(UUID senderId, UUID receiverId);

    RequestsEntity findBySenderIdAndReceiverId(UUID senderId, UUID receiverId);

    List<RequestsEntity> findBySenderIdAndStatusAndIsRequestDeletedIsFalse(UUID senderId, RequestState state);

    List<RequestsEntity> findByReceiverIdAndStatusAndIsRequestDeletedIsFalse(UUID receiverId, RequestState state);
}
