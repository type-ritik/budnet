package com.network.buddy;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HeloWorld {

    private final HelloService helloService;

    public HeloWorld(HelloService service) {
        this.helloService = service;
    }

    @GetMapping("/")
    public String home() {
        return new String(
                "<div style='display:block'><h1>Hello World!</h1><h2>Welcome @type_ritik</h2></div>");
    }

    @GetMapping("/hello")
    public String hello() {
        return helloService.sayHello();
    }

    @GetMapping("/health")
    public String health() {
        return new String(
                "<div style='display:block'><h1>Hello World!</h1><h2>Welcome to health Check</h2></div>");
    }

}
