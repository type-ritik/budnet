package com.network.buddy.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.network.buddy.dto.Requests.RequestsFriendRequest;
import com.network.buddy.model.FriendsEntity;
import com.network.buddy.model.RequestState;
import com.network.buddy.model.RequestsEntity;
import com.network.buddy.model.UserEntity;
import com.network.buddy.repository.FriendsRepository;
import com.network.buddy.repository.RequestsRepository;
import com.network.buddy.repository.UserRepository;
import com.network.buddy.utils.helper.UserValidationUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FriendsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RequestsRepository requestsRepository;

    @Autowired
    private final FriendsRepository friendsRepository;

    public FriendsService(UserRepository _userRepo, RequestsRepository _requestRepo, FriendsRepository _friendRepo) {
        this.userRepository = _userRepo;
        this.requestsRepository = _requestRepo;
        this.friendsRepository = _friendRepo;
    }

    public boolean requestFriend(RequestsFriendRequest request) {
        // Validate SenderId and ReceiverId
        if (!UserValidationUtil.validateUUID(request.senderId())) {
            log.error("Invalid SenderId: " + request.senderId());
            return false;
        }
        if (!UserValidationUtil.validateUUID(request.receiverId())) {
            log.error("Invalid ReceiverId: " + request.receiverId());
            return false;
        }

        UserEntity senderProxy = userRepository.getReferenceById(request.senderId());
        if (senderProxy == null) {
            log.error("Sender not found with ID: " + request.senderId());
            return false;
        }

        UserEntity receiverProxy = userRepository.getReferenceById(request.receiverId());
        if (receiverProxy == null) {
            log.error("Receiver not found with ID: " + request.receiverId());
            return false;
        }

        // Check request record is exists
        if ((requestsRepository.existsBySenderIdAndReceiverId(senderProxy.getId(), receiverProxy.getId()))
                || (requestsRepository.existsBySenderIdAndReceiverId(receiverProxy.getId(), senderProxy.getId()))) {

            RequestsEntity updateRequest = (requestsRepository.existsBySenderIdAndReceiverId(senderProxy.getId(),
                    receiverProxy.getId()))
                            ? requestsRepository.findBySenderIdAndReceiverId(senderProxy.getId(), receiverProxy.getId())
                            : requestsRepository.findBySenderIdAndReceiverId(receiverProxy.getId(),
                                    senderProxy.getId());

            // If request record is deleted = false
            if (!updateRequest.getIsRequestDeleted()) {
                log.error("Active friend request already exists between Sender ID: " + request.senderId() +
                        " and Receiver ID: " + request.receiverId());
                return false;
            } else { // If request record is deleted = true

                // Update request record is deleted = false
                updateRequest.setIsRequestDeleted(false);
                // Update status = pending
                updateRequest.setStatus(RequestState.PENDING);
                // Update Sender and Receiver
                updateRequest.setSender(senderProxy);
                updateRequest.setReceiver(receiverProxy);
                // Update request record
                RequestsEntity revivedRequest = requestsRepository.save(updateRequest);
                if (revivedRequest == null) {
                    log.error("Failed to revive friend request between Sender ID: " + request.senderId() +
                            " and Receiver ID: " + request.receiverId());
                    return false;
                }

                // Notify User
                return true;
            }
        }

        // Create new FollowRequest
        RequestsEntity followRequest = new RequestsEntity();
        followRequest.setReceiver(receiverProxy);
        followRequest.setSender(senderProxy);
        followRequest.setStatus(RequestState.PENDING);

        RequestsEntity response = requestsRepository.save(followRequest);
        if (response == null) {
            log.error("Failed to create friend request between Sender ID: " + request.senderId() +
                    " and Receiver ID: " + request.receiverId());
            return false;
        }

        // Subscribe Receiver
        // Send notification to Receiver Inbox

        return true;
    }

    public boolean unFollowRequest(UUID requestId) {
        // Validate SenderId and ReceiverId
        if (!UserValidationUtil.validateUUID(requestId)) {
            log.error("Invalid SenderId: " + requestId);
            return false;
        }

        // Validate User RequestId

        if (!requestsRepository.existsById(requestId)) {
            log.error("Request not found with ID: " + requestId);
            return false;
        }

        RequestsEntity updateRequest = requestsRepository.getReferenceById(requestId);
        if (updateRequest == null) {
            log.error("Request not found with ID: " + requestId);
            return false;
        }

        if (updateRequest.getIsRequestDeleted()) {
            log.error("Request with ID: " + requestId + " has been already deleted.");
            return false;
        }

        updateRequest.setStatus(RequestState.REJECTED);
        updateRequest.setIsRequestDeleted(true);
        RequestsEntity requestDelete = requestsRepository.save(updateRequest);
        if (requestDelete == null) {
            log.error("Failed to delete friend request with ID: " + requestId);
            return false;
        }

        // Set notification is deleted
        return true;
    }

    public boolean requestResponse(UUID requestId, String response) {
        // Validate requestId
        if (!UserValidationUtil.validateUUID(requestId)) {
            log.error("Invalid RequestId: " + requestId);
            return false;
        }

        // Validation response
        if (!response.equalsIgnoreCase("ACCEPTED") && !response.equalsIgnoreCase("REJECTED")) {
            log.error("Invalid response: " + response);
            return false;
        }

        // Validate requestId
        if (!requestsRepository.existsById(requestId)) {
            log.error("Request not found with ID: " + requestId);
            return false;
        }

        RequestsEntity updateStatus = requestsRepository.getReferenceById(requestId);
        if (updateStatus == null) {
            log.error("Request not found with ID: " + requestId);
            return false;
        }

        if (!updateStatus.getStatus().toString().equalsIgnoreCase("PENDING")) {
            log.error("Request with ID: " + requestId + " is not in PENDING state.");
            return false;
        }

        if (updateStatus.getIsRequestDeleted()) {
            log.error("Request with ID: " + requestId + " has been deleted.");
            return false;
        }

        updateStatus.setStatus(
                (RequestState.ACCEPTED.toString().equalsIgnoreCase("ACCEPTED")) ? RequestState.ACCEPTED
                        : RequestState.REJECTED);

        if ("REJECTED".equalsIgnoreCase(response)) {
            updateStatus.setIsRequestDeleted(true);
            RequestsEntity requestRejected = requestsRepository.save(updateStatus);
            if (requestRejected == null) {
                log.error("Failed to update request status with ID: " + requestId);
                return false;
            }
        } else {
            RequestsEntity requestAccepted = requestsRepository.save(updateStatus);
            if (requestAccepted == null) {
                log.error("Failed to update request status with ID: " + requestId);
                return false;
            }

            // Check if friendship Already exists
            FriendsEntity friendship = friendsRepository.findByRequestId(requestAccepted.getId());
            if (friendship != null) {
                if (friendship.getIsFriendshipDeleted()) {
                    // Revive friendship
                    friendship.setIsFriendshipDeleted(false);
                    FriendsEntity friendshipRevived = friendsRepository.save(friendship);
                    if (friendshipRevived == null) {
                        log.error("Failed to revive friendship for Request ID: " + requestId);
                        return false;
                    }
                }
            } else {
                // Create new friendship
                UserEntity friendAProxy = userRepository.getReferenceById(requestAccepted.getSenderId());
                UserEntity friendBProxy = userRepository.getReferenceById(requestAccepted.getReceiverId());

                FriendsEntity friends = new FriendsEntity();
                friends.setFriendA(friendAProxy);
                friends.setFriendB(friendBProxy);
                friends.setRequest(requestAccepted);

                FriendsEntity buddy = friendsRepository.save(friends);
                if (buddy == null) {
                    log.error("Failed to create friendship between User ID: " + requestAccepted.getSenderId() +
                            " and User ID: " + requestAccepted.getReceiverId());
                    return false;
                }
            }

        }

        // Notify Sender

        return true;
    }

    public boolean unFollowFriend(UUID friendId) {
        // Validate FriendId
        if (!UserValidationUtil.validateUUID(friendId)) {
            log.error("Invalid FriendId: " + friendId);
            return false;
        }

        // Validate friend record
        if (!friendsRepository.existsById(friendId)) {
            log.error("Friendship not found with ID: " + friendId);
            return false;
        }

        FriendsEntity updateFriend = friendsRepository.getReferenceById(friendId);
        if (updateFriend == null) {
            log.error("Friendship not found with ID: " + friendId);
            return false;
        }

        if (updateFriend.getIsFriendshipDeleted()) {
            log.error("Friendship with ID: " + friendId + " has been already deleted.");
            return false;
        }

        // delete friendship
        updateFriend.setIsFriendshipDeleted(true);
        FriendsEntity friendDelete = friendsRepository.save(updateFriend);
        if (friendDelete == null) {
            log.error("Failed to delete friendship with ID: " + friendId);
            return false;
        }

        RequestsEntity deleteRequest = requestsRepository.findRequestsById(friendDelete.getRequestId());
        if (deleteRequest == null) {
            log.error("Failed to find associated request for friendship ID: " + friendId);
            return false;
        }

        if (deleteRequest.getIsRequestDeleted()) {
            log.error("Associated request for friendship ID: " + friendId + " has been already deleted.");
            return false;
        }

        // Delete friendRequest
        deleteRequest.setIsRequestDeleted(true);
        RequestsEntity requestDelete = requestsRepository.save(deleteRequest);
        if (requestDelete == null) {
            log.error("Failed to delete associated request for friendship ID: " + friendId);
            return false;
        }

        // Set notification is deleted
        return true;
    }

    public List<FriendsEntity> listOfFollowers(UUID userId) {
        // Validate userId
        if (!UserValidationUtil.validateUUID(userId)) {
            log.error("Invalid UserId: " + userId);
            return null;
        }

        // Validate User
        if (!userRepository.existsById(userId)) {
            log.error("User not found with ID: " + userId);
            return null;
        }

        List<FriendsEntity> followers = friendsRepository.findByFriendBIdAndIsFriendshipDeletedIsFalse(userId);
        // Check if list is not null
        if (followers == null || followers.isEmpty()) {
            log.info("No followers found for User ID: " + userId);
            return List.of();
        }

        return followers;
    }

    public List<FriendsEntity> listOfFollowings(UUID userId) {
        // Validate userId
        if (!UserValidationUtil.validateUUID(userId)) {
            log.error("Invalid UserId: " + userId);
            return null;
        }

        // Validate user existence
        if (!userRepository.existsById(userId)) {
            log.error("User not found with ID: " + userId);
            return null;
        }

        List<FriendsEntity> followings = friendsRepository.findByFriendAIdAndIsFriendshipDeletedIsTrue(userId);
        // Check if list is not null
        if (followings == null || followings.isEmpty()) {
            log.info("No followings found for User ID: " + userId);
            return List.of();
        }

        return followings;
    }

    public List<RequestsEntity> listOfPendingRequestsBySender(UUID userId) {
        // Validate userId
        if (!UserValidationUtil.validateUUID(userId)) {
            log.error("Invalid UserId: " + userId);
            return null;
        }

        // Validate user existence
        if (!userRepository.existsById(userId)) {
            log.error("User not found with ID: " + userId);
            return null;
        }

        List<RequestsEntity> pendingRequests = requestsRepository
                .findBySenderIdAndStatusAndIsRequestDeletedIsFalse(userId, RequestState.PENDING);

        // Check if list is not null
        if (pendingRequests == null || pendingRequests.isEmpty()) {
            log.info("No pending requests found for User ID: " + userId);
            return List.of();
        }

        return pendingRequests;
    }

    public List<RequestsEntity> listOfPendingRequestsByReceiver(UUID userId) {
        // Validate userId
        if (!UserValidationUtil.validateUUID(userId)) {
            log.error("Invalid UserId: " + userId);
            return null;
        }

        // Validate user existence
        if (!userRepository.existsById(userId)) {
            log.error("User not found with ID: " + userId);
            return null;
        }

        List<RequestsEntity> pendingRequests = requestsRepository
                .findByReceiverIdAndStatusAndIsRequestDeletedIsFalse(userId, RequestState.PENDING);

        // Check if list is not null
        if (pendingRequests == null || pendingRequests.isEmpty()) {
            log.info("No pending requests found for User Id: " + userId);
            return List.of();
        }

        return pendingRequests;

    }
}
