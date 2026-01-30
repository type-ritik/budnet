package com.network.buddy.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.network.buddy.dto.PrivateRoom.PrivateChatRoomRequest;
import com.network.buddy.model.ApiResponse;
import com.network.buddy.model.PrivateChatRoomEntitiy;
import com.network.buddy.service.ChatService;
import com.network.buddy.utils.ResponseUtil;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/room")
public class ChatController {

    private ChatService chatService;

    public ChatController(ChatService _chatService) {
        this.chatService = _chatService;
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<PrivateChatRoomEntitiy>> createRoom(
            @Valid @RequestBody PrivateChatRoomRequest request) {
        // Create Chatroom hit
        PrivateChatRoomEntitiy response = chatService.createChatroom(request.senderId(), request.receiverId());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.success(response, "Room created successfully!", "/api/v1/room"));
    }

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<PrivateChatRoomEntitiy>>> chatRooms(@Valid @RequestBody UUID userId) {
        // Get Chatrooms hit
        List<PrivateChatRoomEntitiy> response = chatService.getChatrooms(userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ResponseUtil.success(response, "Chatrooms fetched successfully!", "/api/v1/room"));
    }

}
