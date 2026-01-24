package com.network.buddy.websocket;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/uuid")
public class generateRandomUUID {

    @GetMapping("/random")
    public String getRandomUUID() {
        return java.util.UUID.randomUUID().toString();
    }
}
