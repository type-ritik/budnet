package com.network.buddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.network.buddy.dto.AuthenticateUserRequest;
import com.network.buddy.dto.AuthenticateUserResponse;
import com.network.buddy.dto.RegisterUserRequest;
import com.network.buddy.dto.RegisterUserResponse;
import com.network.buddy.model.UserEntity;
import com.network.buddy.repository.UserRepository;

@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository _userRepository) {
        this.userRepository = _userRepository;
    }

    public RegisterUserResponse registerUser(RegisterUserRequest user) {

        // business rule here...
        if (user.email() == null) {
            throw new IllegalArgumentException("Email is required");
        }

        if (user.password() == null) {
            throw new IllegalArgumentException("Password is required");
        }

        if (user.password().length() < 8) {
            throw new IllegalArgumentException("Password length must be greater than 8");
        }

        if (user.username().length() < 3) {
            throw new IllegalArgumentException("Username length must be greater than 3");
        }

        UserEntity newUser = new UserEntity();
        newUser.setEmail(user.email());
        newUser.setUsername(user.username());
        newUser.setName(user.name());
        newUser.setPassword(passwordEncoder.encode(user.password()));

        UserEntity savedUser = userRepository.save(newUser);

        RegisterUserResponse response = new RegisterUserResponse(savedUser);

        return response;
    }

    public AuthenticateUserResponse authenticateUser(AuthenticateUserRequest user) {
        boolean userExists = userRepository.existsByEmail(user.email());

        if (!userExists) {
            throw new IllegalArgumentException("User with email address doesn't exists.");
        }

        UserEntity userEntity = userRepository.findByEmail(user.email()).get();
        boolean passwordMatches = passwordEncoder.matches(user.password(), userEntity.getPassword());

        if (!passwordMatches) {
            throw new IllegalArgumentException("Invalid login credentials.");
        }

        AuthenticateUserResponse response = new AuthenticateUserResponse(userEntity);
        return response;

    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));
    }
}
