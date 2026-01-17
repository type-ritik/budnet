package com.network.buddy.dto.Requests;

import java.util.UUID;

public record RequestsFriendRequest(
        UUID senderId,
        UUID receiverId) {

}
