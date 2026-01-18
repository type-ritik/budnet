package com.network.buddy.dto.Requests;

import java.util.Date;
import java.util.UUID;

public record RequestsFriendResponse(
        UUID requestId,
        UUID receiverId,
        String status,
        String receiverName,
        Date requestedDate) {
    public RequestsFriendResponse(UUID requestId, UUID receiverId, String status, String receiverName,
            Date requestedDate) {
        this.requestId = requestId;
        this.receiverId = receiverId;
        this.status = status;
        this.receiverName = receiverName;
        this.requestedDate = requestedDate;
    }

}
