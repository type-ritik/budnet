package com.network.buddy.websocket;

import java.security.Principal;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessagesController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    public ChatMessagesController(SimpMessagingTemplate _simpMessagingTemplate) {
        simpMessagingTemplate = _simpMessagingTemplate;
    }

    @MessageMapping("/chat.send")
    @SendTo("/topic/messages")
    public String send(String messageByte) {
        // String message = new String(messageByte, StandardCharsets.UTF_8);
        return messageByte;
    }

    @MessageMapping("/chat.online")
    public void online(ChatMessage message, Principal principal) {
        System.out.println("HELLO MYSELF NEW");
        System.out.println("Message: " + message.getMessage() + " Receiver: " + message.getReceiverId());
        System.out.println("Principal data: " + principal.getName());
        simpMessagingTemplate.convertAndSendToUser(message.getReceiverId(), "/queue/messages", message.getMessage());
    }
}
