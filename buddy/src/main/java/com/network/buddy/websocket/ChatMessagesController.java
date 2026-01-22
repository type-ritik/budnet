package com.network.buddy.websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessagesController {

    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public String send(String messageByte) {
        // String message = new String(messageByte, StandardCharsets.UTF_8);
        return messageByte;
    }
}
