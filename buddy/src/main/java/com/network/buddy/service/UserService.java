package com.network.buddy.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.network.buddy.dto.Authentication.AuthenticateUserRequest;
import com.network.buddy.dto.Authentication.AuthenticateUserResponse;
import com.network.buddy.dto.Registration.RegisterUserRequest;
import com.network.buddy.dto.Registration.RegisterUserResponse;
import com.network.buddy.model.Role;
import com.network.buddy.model.UserEntity;
import com.network.buddy.repository.UserRepository;
import com.network.buddy.utils.exception.ResourceNotFoundException;
import com.network.buddy.utils.exception.ResponseNotFoundException;
import com.network.buddy.utils.helper.UserValidationUtil;
import io.jsonwebtoken.JwtException;
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

        // business rule here...

        // Email validation
        if (UserValidationUtil.validateEmail(user.email())) {
            log.info("Email validated");
        }

        // Name validation
        if (UserValidationUtil.validateName(user.name())) {
            log.info("Name validated");
        }

        // Username validation
        if (UserValidationUtil.validateUsername(user.username())) {
            log.info("Username validated");
        }

        // Password validation
        if (UserValidationUtil.validatePassword(user.password())) {
            log.info("Password validated");
        }

        // User entity
        UserEntity newUser = new UserEntity();
        newUser.setEmail(user.email());
        newUser.setUsername(user.username());
        newUser.setName(user.name());
        newUser.setPassword(passwordEncoder.encode(user.password()));
        newUser.setRole(Role.USER);

        try {

            // Save user entity
            UserEntity savedUser = userRepository.save(newUser);

            log.info("Save user data in table");

            // Generate token
            String token = jwtService.generateToken(savedUser);

            // Error token generate
            if (token == null) {
                throw new ResourceNotFoundException("Error occurred while generating token.");
            }

            log.info("Generate token");

            // Create response
            RegisterUserResponse response = new RegisterUserResponse(savedUser, token);

            log.info("User Registered Successfully");

            // Response
            return response;
        } catch (ResponseNotFoundException e) {
            throw new ResponseNotFoundException("Server error");
        }
    }

    public AuthenticateUserResponse authenticateUser(AuthenticateUserRequest user) {

        // Email validation
        if (UserValidationUtil.validateEmail(user.email())) {
            log.info("Email validated");
        }

        // Password validation
        if (UserValidationUtil.validatePassword(user.password())) {
            log.info("Password validated");
        }

        // Email exists
        if (!userRepository.existsByEmail(user.email())) {
            throw new ResourceNotFoundException("User with email address doesn't exists.");
        }
        log.info("User Exists");

        // Retrieve payload
        UserEntity userEntity = userRepository.findByEmail(user.email());
        log.info("Retrive DB user data");

        // Source of truth
        if (!passwordEncoder.matches(user.password(), userEntity.getPassword())) {
            throw new ResourceNotFoundException("Invalid login credentials.");
        }
        log.info("Password matched");

        // Generate token
        String token = jwtService.generateToken(userEntity);
        log.info("Token generated");

        // Error token generate
        if (token == null) {
            throw new ResourceNotFoundException("Error occurred while generating token.");
        }

        try {

            log.info("Response is creating");
            // Create response
            AuthenticateUserResponse response = new AuthenticateUserResponse(userEntity, token);
            log.info("Authentication Successfull");

            // Response
            return response;
        } catch (ResponseNotFoundException e) {
            throw new ResponseNotFoundException("Server error");
        }

    }

    public UserEntity getUserById(UUID id) {
        // UUID validation
        if (UserValidationUtil.validateUUID(id)) {
            log.info("UUID validated");
        }

        try {

            // Search user by UUID
            UserEntity response = userRepository.findUserById(id);

            if (response.toString().isEmpty() || response == null) {
                throw new ResourceNotFoundException("User not found with id: " + id);
            } else {
                log.info("User found with id: " + id);
                return response;
            }

        } catch (JwtException e) {
            throw new JwtException("Error: " + e.getLocalizedMessage());
        }
    }

    public UserEntity loadUserByUsername(String username) {
        if (UserValidationUtil.validateUsername(username)) {
            log.info("Username validated");
        }
        try {

            // Search user by username
            UserEntity user = userRepository.findByUsername(username);

            // Response
            return user;
        } catch (ResponseNotFoundException e) {
            throw new ResponseNotFoundException("Server error");
        }

    }
}
