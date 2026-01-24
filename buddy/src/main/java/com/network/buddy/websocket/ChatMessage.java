package com.network.buddy.websocket;

public class ChatMessage {
    private String message;
    private String receiverId;

    public ChatMessage(String _message, String _receiverId) {
        this.message = _message;
        this.receiverId = _receiverId;
    }

    // getters & setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

}
