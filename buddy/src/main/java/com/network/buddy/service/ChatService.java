package com.network.buddy.service;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.network.buddy.model.PrivateChatRoomEntitiy;
import com.network.buddy.model.UserEntity;
import com.network.buddy.repository.FriendsRepository;
import com.network.buddy.repository.PrivateChatroomRepository;
import com.network.buddy.repository.UserRepository;
import com.network.buddy.utils.helper.UserValidationUtil;

@Service
public class ChatService {

    private UserRepository userRepository;

    private PrivateChatroomRepository chatroomRepository;

    private FriendsRepository friendsRepository;

    public ChatService() {
    }

    public ChatService(UserRepository _userRepository,
            PrivateChatroomRepository _chatroomRepository, FriendsRepository _friendsRepository) {
        this.userRepository = _userRepository;
        this.chatroomRepository = _chatroomRepository;
        this.friendsRepository = _friendsRepository;
    }

    public PrivateChatRoomEntitiy createChatroom(UUID senderId, UUID receiverId) {

        if (!UserValidationUtil.validateUUID(receiverId)) {
            throw new IllegalArgumentException("Invalid receiver ID");
        }

        if (!UserValidationUtil.validateUUID(senderId)) {
            throw new IllegalArgumentException("Invalid sender ID");
        }

        if (!userRepository.existsById(receiverId)) {
            throw new IllegalArgumentException("Invalid Receiver");
        }

        if (!userRepository.existsById(senderId)) {
            throw new IllegalArgumentException("Invalid Sender");
        }

        if (!friendsRepository.existsByFriendAIdAndFriendBIdAndFriendshipDeletedIsFalse(senderId, receiverId)) {
            throw new IllegalArgumentException("Users are not friends");
        }

        if (!friendsRepository.existsByFriendAIdAndFriendBIdAndFriendshipDeletedIsFalse(receiverId, senderId)) {
            throw new IllegalArgumentException("Users are not friends");
        }

        UserEntity senderProxy = userRepository.getReferenceById(senderId);
        UserEntity receiverProxy = userRepository.getReferenceById(receiverId);

        PrivateChatRoomEntitiy room = new PrivateChatRoomEntitiy(senderProxy, receiverProxy);

        PrivateChatRoomEntitiy response = chatroomRepository.save(room);

        if (response == null) {
            throw new IllegalStateException("Failed to create chatroom");
        }

        return response;
    }

    public List<PrivateChatRoomEntitiy> getChatrooms(UUID userId) {
        if (!UserValidationUtil.validateUUID(userId)) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if (!userRepository.existsById(userId)) {
            throw new IllegalArgumentException("User does not exist");
        }

        List<PrivateChatRoomEntitiy> chatrooms = chatroomRepository.findAll().stream()
                .filter(room -> room.getSenderId().equals(userId) || room.getReceiverId().equals(userId))
                .toList();

        return chatrooms;
    }

}
