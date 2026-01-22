package com.network.buddy.websocket;

import java.util.UUID;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;
import com.network.buddy.UserRemember.RememberUser;
import jakarta.websocket.SessionException;

@Component
public class WsEvents {

    private RememberUser rememberUser;

    public WsEvents(RememberUser _rememberUser) {
        this.rememberUser = _rememberUser;
    }

    @EventListener
    public void onConnect(SessionConnectEvent e) {
        System.out.println("Connected");
    }

    @EventListener
    public void onConnected(SessionConnectedEvent e) {
        System.out.println("Connection Established");
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent e) {
        System.out.println("Disconnected");
    }

    @EventListener
    public void onSubscribe(SessionSubscribeEvent e) {
        UUID sessionId = UUID.fromString(e.getMessage().getHeaders().get("simpSessionId").toString());
        String userId = e.getMessage().getHeaders().get("simpSubscriptionId").toString();
        // UUID userId =
        // UUID.fromString(e.getMessage().getHeaders().get("simpSubscriptionId").toString());
        rememberUser.setNewSubscription(userId, sessionId);
        System.out.println("Subscribed");
    }

    @EventListener
    public void onUnsubscribe(SessionUnsubscribeEvent e) {
        System.out.println("Unsubscribed");
    }

    @EventListener
    public void onPing(SessionException e) {
        System.out.println("Error");
    }
}
