package com.network.buddy.dto.PrivateRoom;

import java.util.UUID;

public record PrivateChatRoomRequest(
        UUID senderId,
        UUID receiverId) {
}
