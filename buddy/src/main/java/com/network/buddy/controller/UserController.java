package com.network.buddy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.network.buddy.dto.AuthenticateUserRequest;
import com.network.buddy.dto.AuthenticateUserResponse;
import com.network.buddy.dto.RegisterUserRequest;
import com.network.buddy.dto.RegisterUserResponse;
import com.network.buddy.model.UserEntity;
import com.network.buddy.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/auth/v1")
public class UserController {

    private final UserService userService;

    public UserController(UserService _userService) {
        this.userService = _userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody RegisterUserRequest request) {
        try {
            RegisterUserResponse response = userService.registerUser(request);
            System.out.println("Signup successful!");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getLocalizedMessage());
        }

    }

    @GetMapping("user/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable Long id) {
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticateUserRequest request) {
        try {

            AuthenticateUserResponse response = userService.authenticateUser(request);
            System.out.println("Login successful!");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getLocalizedMessage());
        }
    }

}
