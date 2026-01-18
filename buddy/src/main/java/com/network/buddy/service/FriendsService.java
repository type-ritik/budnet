package com.network.buddy.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.network.buddy.dto.Requests.RequestsFriendRequest;
import com.network.buddy.dto.Requests.RequestsFriendResponse;
import com.network.buddy.model.FriendsEntity;
import com.network.buddy.model.RequestState;
import com.network.buddy.model.RequestsEntity;
import com.network.buddy.model.UserEntity;
import com.network.buddy.repository.FriendsRepository;
import com.network.buddy.repository.RequestsRepository;
import com.network.buddy.repository.UserRepository;
import com.network.buddy.utils.exception.ResourceNotFoundException;
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

    public RequestsFriendResponse requestFriend(RequestsFriendRequest request) {
        // Validate SenderId and ReceiverId
        if (!UserValidationUtil.validateUUID(request.senderId())) {
            log.error("Invalid SenderId: " + request.senderId());
            throw new ResourceNotFoundException("Illigal UserId");
        }
        if (!UserValidationUtil.validateUUID(request.receiverId())) {
            log.error("Invalid ReceiverId: " + request.receiverId());
            throw new ResourceNotFoundException("Illigal receiverId");
        }

        UserEntity senderProxy = userRepository.getReferenceById(request.senderId());
        if (senderProxy == null) {
            log.error("Sender not found with ID: " + request.senderId());
            throw new ResourceNotFoundException("Sender not found.");
        }

        UserEntity receiverProxy = userRepository.getReferenceById(request.receiverId());
        if (receiverProxy == null) {
            log.error("Receiver not found with ID: " + request.receiverId());
            throw new ResourceNotFoundException("Receiver not found.");
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
                throw new ResourceNotFoundException("Friend request already exists");
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
                    throw new ResourceNotFoundException("Failed to revive friend request.");
                }

                // Notify User
                RequestsFriendResponse response = new RequestsFriendResponse(revivedRequest.getId(),
                        revivedRequest.getReceiver().getId(), revivedRequest.getStatus().toString(),
                        revivedRequest.getReceiver().getName(), revivedRequest.getRequestedAt());
                return response;
            }
        }

        // Create new FollowRequest
        RequestsEntity followRequest = new RequestsEntity();
        followRequest.setReceiver(receiverProxy);
        followRequest.setSender(senderProxy);
        followRequest.setStatus(RequestState.PENDING);

        RequestsEntity reqResponse = requestsRepository.save(followRequest);
        if (reqResponse == null) {
            log.error("Failed to create friend request between Sender ID: " + request.senderId() +
                    " and Receiver ID: " + request.receiverId());
            throw new ResourceNotFoundException("Failed to create friend request.");
        }

        // Subscribe Receiver
        // Send notification to Receiver Inbox

        RequestsFriendResponse response = new RequestsFriendResponse(reqResponse.getId(),
                reqResponse.getReceiver().getId(), reqResponse.getStatus().toString(),
                reqResponse.getReceiver().getName(), reqResponse.getRequestedAt());
        return response;
    }

    public boolean unFollowRequest(UUID requestId, UUID userId) {
        // Validate SenderId and ReceiverId
        if (!UserValidationUtil.validateUUID(requestId)) {
            log.error("Invalid RequestId: " + requestId);
            throw new ResourceNotFoundException("Invalid RequestId");
        }

        // Validate User RequestId

        if (!requestsRepository.existsById(requestId)) {
            log.error("Request not found with ID: " + requestId);
            throw new ResourceNotFoundException("Request not found with requestedId");
        }

        RequestsEntity updateRequest = requestsRepository.getReferenceById(requestId);
        if (updateRequest == null) {
            log.error("Request not found with ID: " + requestId);
            throw new ResourceNotFoundException("Request not found");
        }

        if (!updateRequest.getSenderId().equals(userId)) {
            log.error("User with ID: " + userId + " is not authorized to delete this request.");
            throw new ResourceNotFoundException("User is not authorized to delete this request");
        }

        if (updateRequest.getIsRequestDeleted()) {
            log.error("Request with ID: " + requestId + " has been already deleted.");
            throw new ResourceNotFoundException("Request has been already deprecated");
        }

        updateRequest.setStatus(RequestState.REJECTED);
        updateRequest.setIsRequestDeleted(true);
        RequestsEntity requestDelete = requestsRepository.save(updateRequest);
        if (requestDelete == null) {
            log.error("Failed to delete friend request with ID: " + requestId);
            throw new ResourceNotFoundException("Failed to delete friend request");
        }

        // Set notification is deleted
        return true;
    }

    public boolean requestResponse(UUID requestId, String response) {
        // Validate requestId
        if (!UserValidationUtil.validateUUID(requestId)) {
            log.error("Invalid RequestId: " + requestId);
            throw new ResourceNotFoundException("Invalid RequestId");
        }

        // Validation response
        if (!response.equalsIgnoreCase("ACCEPTED") && !response.equalsIgnoreCase("REJECTED")) {
            log.error("Invalid response: " + response);
            throw new ResourceNotFoundException("Invalid response");
        }

        // Validate requestId
        if (!requestsRepository.existsById(requestId)) {
            log.error("Request not found with ID: " + requestId);
            throw new ResourceNotFoundException("Request not found with ID");
        }

        RequestsEntity updateStatus = requestsRepository.getReferenceById(requestId);
        if (updateStatus == null) {
            log.error("Request not found with ID: " + requestId);
            throw new ResourceNotFoundException("Request not found");
        }

        if (!updateStatus.getStatus().toString().equalsIgnoreCase("PENDING")) {
            log.error("Request with ID: " + requestId + " is not in PENDING state.");
            throw new ResourceNotFoundException("Request is not in PENDING state");
        }

        if (updateStatus.getIsRequestDeleted()) {
            log.error("Request with ID: " + requestId + " has been deleted.");
            throw new ResourceNotFoundException("Request isn't exists");
        }

        updateStatus.setStatus(
                (RequestState.ACCEPTED.toString().equalsIgnoreCase(response.toUpperCase())) ? RequestState.ACCEPTED
                        : RequestState.REJECTED);

        if ("REJECTED".equalsIgnoreCase(response)) {
            updateStatus.setIsRequestDeleted(true);
            RequestsEntity requestRejected = requestsRepository.save(updateStatus);
            if (requestRejected == null) {
                log.error("Failed to update request status with ID: " + requestId);
                throw new ResourceNotFoundException("Failed to update request status");
            }
        } else {
            RequestsEntity requestAccepted = requestsRepository.save(updateStatus);
            if (requestAccepted == null) {
                log.error("Failed to update request status with ID: " + requestId);
                throw new ResourceNotFoundException("Failed to update request status");
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
                        throw new ResourceNotFoundException("Failed to revive friendship");
                    }
                }
            } else {
                // Create new friendship
                UserEntity friendAProxy = userRepository.getReferenceById(requestAccepted.getSenderId());
                UserEntity friendBProxy = userRepository.getReferenceById(requestAccepted.getReceiverId());
                log.info("hello world");

                FriendsEntity friends = new FriendsEntity();
                friends.setFriendA(friendAProxy);
                friends.setFriendB(friendBProxy);
                friends.setRequest(requestAccepted);

                FriendsEntity buddy = friendsRepository.save(friends);
                if (buddy == null) {
                    log.error("Failed to create friendship between User ID: " + requestAccepted.getSenderId() +
                            " and User ID: " + requestAccepted.getReceiverId());
                    throw new ResourceNotFoundException("Failed to create friendship");
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
            throw new ResourceNotFoundException("Invalid FriendId");
        }

        // Validate friend record
        if (!friendsRepository.existsById(friendId)) {
            log.error("Friendship not found with ID: " + friendId);
            throw new ResourceNotFoundException("Friendship not found with friendId");
        }

        FriendsEntity updateFriend = friendsRepository.getReferenceById(friendId);
        if (updateFriend == null) {
            log.error("Friendship not found with ID: " + friendId);
            throw new ResourceNotFoundException("Friendship not found");
        }

        if (updateFriend.getIsFriendshipDeleted()) {
            log.error("Friendship with ID: " + friendId + " has been already deleted.");
            throw new ResourceNotFoundException("Friendship is already deleted");
        }

        // delete friendship
        updateFriend.setIsFriendshipDeleted(true);
        FriendsEntity friendDelete = friendsRepository.save(updateFriend);
        if (friendDelete == null) {
            log.error("Failed to delete friendship with ID: " + friendId);
            throw new ResourceNotFoundException("Failed to delete friendship with friendId");
        }

        RequestsEntity deleteRequest = requestsRepository.findRequestsById(friendDelete.getRequestId());
        if (deleteRequest == null) {
            log.error("Failed to find associated request for friendship ID: " + friendId);
            throw new ResourceNotFoundException("Failed to find associated request for friendship");
        }

        if (deleteRequest.getIsRequestDeleted()) {
            log.error("Associated request for friendship ID: " + friendId + " has been already deleted.");
            throw new ResourceNotFoundException("Associated request for friendship is already deleted");
        }

        // Delete friendRequest
        deleteRequest.setIsRequestDeleted(true);
        deleteRequest.setStatus(RequestState.REJECTED);
        RequestsEntity requestDelete = requestsRepository.save(deleteRequest);
        if (requestDelete == null) {
            log.error("Failed to delete associated request for friendship ID: " + friendId);
            throw new ResourceNotFoundException("Failed to delete associated request");
        }

        // Set notification is deleted
        return true;
    }

    public List<FriendsEntity> listOfFollowers(UUID userId) {
        // Validate userId
        if (!UserValidationUtil.validateUUID(userId)) {
            log.error("Invalid UserId: " + userId);
            throw new ResourceNotFoundException("Invalid userId");
        }

        // Validate User
        if (!userRepository.existsById(userId)) {
            log.error("User not found with ID: " + userId);
            throw new ResourceNotFoundException("User not found");
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
            throw new ResourceNotFoundException("Invalid userId");
        }

        // Validate user existence
        if (!userRepository.existsById(userId)) {
            log.error("User not found with ID: " + userId);
            throw new ResourceNotFoundException("User not found");
        }

        List<FriendsEntity> followings = friendsRepository.findByFriendAIdAndIsFriendshipDeletedIsFalse(userId);
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
            throw new ResourceNotFoundException("Invalid userId");
        }

        // Validate user existence
        if (!userRepository.existsById(userId)) {
            log.error("User not found with ID: " + userId);
            throw new ResourceNotFoundException("User not found");
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
            throw new ResourceNotFoundException("Invalid userId");
        }

        // Validate user existence
        if (!userRepository.existsById(userId)) {
            log.error("User not found with ID: " + userId);
            throw new ResourceNotFoundException("User not found");
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
