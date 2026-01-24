package com.network.buddy.websocket;

import java.security.Principal;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import com.network.buddy.UserRemember.RememberUser;

public class CustomHandshakeHandler extends DefaultHandshakeHandler {

    private RememberUser rememberUser;

    @Override
    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler,
            Map<String, Object> attributes) {
        System.out.println("Request: " + request.getHeaders().toString());

        final String name = UUID.randomUUID().toString();
        return () -> name;
    }
}
