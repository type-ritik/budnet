package com.network.buddy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.network.buddy.dto.AuthenticateUserRequest;
import com.network.buddy.dto.AuthenticateUserResponse;
import com.network.buddy.dto.RegisterUserRequest;
import com.network.buddy.dto.RegisterUserResponse;
import com.network.buddy.model.UserEntity;
import com.network.buddy.service.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping("/api/auth/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService _userService) {
        this.userService = _userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterUserRequest request) {
        try {
            log.info("Signup component hit");
            log.info("Request info: " + request.toString());
            RegisterUserResponse response = userService.registerUser(request);
            log.info("Signup component end");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getLocalizedMessage());
        }

    }

    @GetMapping("user/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody AuthenticateUserRequest request) {
        try {
            System.out.println("User login start");
            AuthenticateUserResponse response = userService.authenticateUser(request);
            System.out.println("Login successful!");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getLocalizedMessage());
        }
    }

}
