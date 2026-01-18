package com.network.buddy.controller;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.network.buddy.dto.Requests.RequestsFriendRequest;
import com.network.buddy.dto.Requests.RequestsFriendResponse;
import com.network.buddy.model.ApiResponse;
import com.network.buddy.model.FriendsEntity;
import com.network.buddy.model.RequestsEntity;
import com.network.buddy.service.FriendsService;
import com.network.buddy.service.JwtService;
import com.network.buddy.utils.ResponseUtil;
import com.network.buddy.utils.exception.ResourceNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/friends")
public class FriendController {

    private final FriendsService friendsService;

    private final JwtService jwtService;

    public FriendController(FriendsService _friendsService, JwtService _jwtService) {
        this.friendsService = _friendsService;
        this.jwtService = _jwtService;
    }

    @PostMapping("/requests")
    public ResponseEntity<ApiResponse<RequestsFriendResponse>> sendRequest(
            @RequestHeader(name = "Authorization") String header, @RequestBody UUID receiverId) {

        log.info("Received friend request to user with ID: {}", receiverId);
        log.info(" id: " + receiverId);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7); // Extracts the part after "Bearer
            UUID userId = jwtService.extractId(token);

            // Create RequestDTO
            RequestsFriendRequest request = new RequestsFriendRequest(userId, receiverId);

            // Call the Request Service
            RequestsFriendResponse response = friendsService.requestFriend(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ResponseUtil.success(response, "Request sent successfully!", "/requests"));
        } else {
            // Handle case where Authorization header is missing or malformed s
            throw new ResourceNotFoundException("Missing orinvalid Authorization header");
        }
    }

    @PostMapping("/responses")
    public ResponseEntity<ApiResponse<Boolean>> sendResponse(@RequestHeader(name = "Authorization") String header,
            @RequestBody Map<String, Object> payload) {
        String response = payload.get("response").toString();
        UUID requestId = UUID.fromString(payload.get("requestId").toString());
        log.info("Received friend request response for request ID: {}", requestId);

        boolean result = friendsService.requestResponse(requestId, response);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseUtil.success(result, "Response processed successfully!", "/response"));
    }

    @GetMapping("/requests/senders")
    public ResponseEntity<ApiResponse<List<RequestsEntity>>> requestListSender(
            @RequestHeader(name = "Authorization") String header) {
        log.info("Fetching send requests to the user");
        UUID userId = jwtService.extractId(header);
        // Fetch pending request
        List<RequestsEntity> response = friendsService.listOfPendingRequestsBySender(userId);

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseUtil.success(response, "Pending following requests fetched successfully!",
                        "/requests/senders"));
    }

    @GetMapping("/requests/receivers")
    public ResponseEntity<ApiResponse<List<RequestsEntity>>> requestListReceiver(
            @RequestHeader(name = "Authorization") String header) {
        log.info("Fetching friends requests for the user");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            UUID userId = jwtService.extractId(token);
            // Fetch pending friend request
            List<RequestsEntity> response = friendsService.listOfPendingRequestsByReceiver(userId);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ResponseUtil.success(response, "Pending Friend requests fetched successfully!",
                            "/requests/receivers"));
        } else {
            throw new ResourceNotFoundException("Missing orinvalid Authorization header");
        }
    }

    @PostMapping("/requests/deprecate/{request_id}")
    public ResponseEntity<ApiResponse<Boolean>> deprecateRequest(@PathVariable("request_id") UUID requestId,
            @RequestHeader(name = "Authorization") String header) {
        log.info("Deprecating friend request with ID: {}", requestId);

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            UUID userId = jwtService.extractId(token);
            boolean result = friendsService.unFollowRequest(requestId, userId);
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ResponseUtil.success(result, "Request deprecated successfully!", "/requests/deprecate"));
        } else {
            throw new ResourceNotFoundException("Missing or invalid Authorization header");
        }

    }

    @PostMapping("/deprecate/{friend_id}")
    public ResponseEntity<ApiResponse<Boolean>> deprecateFriendship(@PathVariable("friend_id") UUID friendId) {
        log.info("Deprecating friendship with ID: {}", friendId);

        boolean result = friendsService.unFollowFriend(friendId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseUtil.success(result, "Friendship deprecated successfully!", "/deprecate"));
    }

    @GetMapping("/followers")
    public ResponseEntity<ApiResponse<List<FriendsEntity>>> followersList(
            @RequestHeader(name = "Authorization") String header) {
        log.info("Fetching followers list for the user");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            UUID userId = jwtService.extractId(token);
            // Fetch followers
            List<FriendsEntity> response = friendsService.listOfFollowers(userId);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ResponseUtil.success(response, "Followers list fetched successfully!", "/followers"));
        } else {
            throw new ResourceNotFoundException("Missing or invalid Authorization header");
        }
    }

    @GetMapping("/followings")
    public ResponseEntity<ApiResponse<List<FriendsEntity>>> followingList(
            @RequestHeader(name = "Authorization") String header) {
        log.info("Fetching following list for the user");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            UUID userId = jwtService.extractId(token);
            // Fetch following
            List<FriendsEntity> response = friendsService.listOfFollowings(userId);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ResponseUtil.success(response, "Following list fetched successfully!", "/following"));
        } else {
            throw new ResourceNotFoundException("Missing or invalid Authorization header");
        }
    }
}
