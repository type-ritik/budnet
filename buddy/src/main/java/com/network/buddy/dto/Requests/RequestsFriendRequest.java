package com.network.buddy.dto.Requests;

import java.util.UUID;

public record RequestsFriendRequest(
                UUID senderId,
                UUID receiverId) {

        public RequestsFriendRequest(UUID senderId, UUID receiverId) {
                this.senderId = senderId;
                this.receiverId = receiverId;
        }

}
