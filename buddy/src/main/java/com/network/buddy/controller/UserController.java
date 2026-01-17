package com.network.buddy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.network.buddy.dto.Authentication.AuthenticateUserRequest;
import com.network.buddy.dto.Authentication.AuthenticateUserResponse;
import com.network.buddy.dto.Registration.RegisterUserRequest;
import com.network.buddy.dto.Registration.RegisterUserResponse;
import com.network.buddy.model.ApiResponse;
import com.network.buddy.model.UserEntity;
import com.network.buddy.service.UserService;
import com.network.buddy.utils.ResponseUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService _userService) {
        this.userService = _userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<RegisterUserResponse>> signup(@Valid @RequestBody RegisterUserRequest request) {
        log.info("Signup component hit");
        RegisterUserResponse response = userService.registerUser(request);
        log.info("Signup component end");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseUtil.success(response, "Welcome " + response.name(), "/"));

    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserEntity>> getUser(@PathVariable UUID id) {

        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(ResponseUtil.success(user, "User extracted successfully!", "/"));

    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticateUserResponse>> login(
            @Valid @RequestBody AuthenticateUserRequest request) {

        System.out.println("User login start");
        AuthenticateUserResponse response = userService.authenticateUser(request);
        System.out.println("Login successful!");
        return ResponseEntity.ok(ResponseUtil.success(response, "Welcome back " + response.name(), "/"));

    }

}
