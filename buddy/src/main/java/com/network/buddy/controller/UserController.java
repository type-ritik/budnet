package com.network.buddy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.network.buddy.model.UserEntity;
import com.network.buddy.repository.UserRepository;
import com.network.buddy.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private final UserRepository userRepository;

    private final UserService userService;

    public UserController(UserService _userService, UserRepository userRepository) {
        this.userService = _userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/auth/user/signup")
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {

        UserEntity savedUser = userService.createuser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping("/get/user/{id}")
    public ResponseEntity<UserEntity> getUser(@PathVariable Long id) {
        UserEntity user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
}
