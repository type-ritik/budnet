package com.network.buddy.UserRemember;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RememberUser {
    private ConcurrentHashMap<UUID, UUID> registerUser = new ConcurrentHashMap<>();

    public RememberUser() {
    }

    public void setNewSubscription(UUID userId, UUID sessionId) {
        if (this.registerUser.containsKey(userId)) {
            registerUser.replace(userId, sessionId);
        } else {
            this.registerUser.put(userId, sessionId);
        }
    }

    public void setRemoveSubscription(UUID userId, UUID sessionId) {
        if (this.registerUser.containsKey(userId)) {
            this.registerUser.remove(userId, sessionId);
        }
    }

    public UUID getSessionIdByUserId(UUID userId) {
        if (this.registerUser.containsKey(userId)) {
            return this.registerUser.get(userId);
        }
        return null;
    }

    public UUID getUserIdBySessionId(UUID sessionId) {
        if (this.registerUser.containsValue(sessionId)) {
            for (UUID key : this.registerUser.keySet()) {
                if (this.registerUser.get(key).equals(sessionId)) {
                    return key;
                }
            }
        }
        return null;
    }
}
