package com.network.buddy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public UserEntity createuser(UserEntity user) {

        // business rule here...
        if (user.getEmail() == null) {
            throw new IllegalArgumentException("Email is required");
        }

        if (user.getPassword() == null) {
            throw new IllegalArgumentException("Password is required");
        }

        if (user.getPassword().length() < 8) {
            throw new IllegalArgumentException("Password length must be greater than 8");
        }

        if (user.getUsername().length() < 3) {
            throw new IllegalArgumentException("Username length must be greater than 3");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found."));
    }
}
