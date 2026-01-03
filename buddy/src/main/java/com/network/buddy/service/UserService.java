package com.network.buddy.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.network.buddy.dto.AuthenticateUserRequest;
import com.network.buddy.dto.AuthenticateUserResponse;
import com.network.buddy.dto.RegisterUserRequest;
import com.network.buddy.dto.RegisterUserResponse;
import com.network.buddy.model.Role;
import com.network.buddy.model.UserEntity;
import com.network.buddy.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository _userRepository, PasswordEncoder _passwordEncoder, JwtService _jwtService) {
        this.userRepository = _userRepository;
        this.passwordEncoder = _passwordEncoder;
        this.jwtService = _jwtService;
    }

    public RegisterUserResponse registerUser(RegisterUserRequest user) {

        log.info("User registration start");

        // business rule here...
        if (user.email() == null) {
            throw new IllegalArgumentException("Email is required");
        }
        log.info("Valid email");

        if (user.password() == null) {
            throw new IllegalArgumentException("Password is required");
        }

        if (user.password().length() < 8) {
            throw new IllegalArgumentException("Password length must be greater than 8");
        }

        log.info("Valid password");

        if (user.username().length() < 3) {
            throw new IllegalArgumentException("Username length must be greater than 3");
        }

        log.info("Valid username");

        UserEntity newUser = new UserEntity();
        newUser.setEmail(user.email());
        newUser.setUsername(user.username());
        newUser.setName(user.name());
        newUser.setPassword(passwordEncoder.encode(user.password()));
        newUser.setRole(Role.USER);

        UserEntity savedUser = userRepository.save(newUser);

        log.info("Save user data in table");

        String token = jwtService.generateToken(savedUser);

        log.info("Generate token: " + token);

        RegisterUserResponse response = new RegisterUserResponse(savedUser, token);

        log.info("Response is created: " + response.toString());
        log.info("User Registered Successfully");

        return response;
    }

    public AuthenticateUserResponse authenticateUser(AuthenticateUserRequest user) {
        log.info("Authentication start");
        boolean userExists = userRepository.existsByEmail(user.email());

        if (!userExists) {
            throw new IllegalArgumentException("User with email address doesn't exists.");
        }
        log.info("User Exists");

        UserEntity userEntity = userRepository.findByEmail(user.email());
        log.info("Retrive DB user data");

        boolean passwordMatches = passwordEncoder.matches(user.password(), userEntity.getPassword());
        log.info("Password matching start");

        if (!passwordMatches) {
            throw new IllegalArgumentException("Invalid login credentials.");
        }
        log.info("Password matched");

        String token = jwtService.generateToken(userEntity);
        log.info("Token generated");

        log.info("Response is creating");
        AuthenticateUserResponse response = new AuthenticateUserResponse(userEntity, token);
        log.info("Authentication Successfull");

        return response;

    }

    public UserEntity getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));
    }

    public UserEntity loadUserByUsername(String username) {
        if (username.length() < 3) {
            throw new IllegalArgumentException("Username must be at least 3 Character long.");
        }
        UserEntity user = userRepository.findByUsername(username);

        if (user.getName() == null) {
            throw new IllegalArgumentException("User not found with username: " + username);
        }

        return user;
    }
}
