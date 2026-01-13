package com.network.buddy.utils.helper;

import java.util.UUID;

import com.network.buddy.utils.exception.ResourceNotFoundException;

public class UserValidationUtil {
    public static boolean validateName(String name) {
        // Name validation regex
        String pattern = "(^[a-zA-Z][a-zA-Z\\s]{2,})";

        if (name.isEmpty()) {
            throw new ResourceNotFoundException("Name is required");
        }

        if (name.length() < 3 || name.length() > 30) {
            throw new ResourceNotFoundException("Name must be 3 character long");
        }

        if (!name.matches(pattern)) {
            throw new ResourceNotFoundException("Invalid name");
        }
        return true;
    }

    public static boolean validateUsername(String username) {
        // Username validation regex
        String pattern = "(^[a-z][\\w]{2,})";

        if (username.isEmpty()) {
            throw new ResourceNotFoundException("Username is required");
        }

        if (username.length() < 3 || username.length() > 16) {
            throw new ResourceNotFoundException("Username must be 3 character long");
        }

        if (!username.matches(pattern)) {
            throw new ResourceNotFoundException("Invalid username");
        }

        return true;
    }

    public static boolean validateEmail(String email) {
        // Email validation regex
        String pattern = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

        if (email.isEmpty()) {
            throw new ResourceNotFoundException("Email is required");
        }

        if (email.length() < 6) {
            throw new ResourceNotFoundException("Email must be 6 characters long");
        }

        if (!email.matches(pattern)) {
            throw new ResourceNotFoundException("Invalid email");
        }

        return true;
    }

    public static boolean validatePassword(String password) {
        // Password validation regex
        String patten = "[\\w]{8,}";

        if (password.isEmpty()) {
            throw new ResourceNotFoundException("Password is required");
        }

        if (password.length() < 8) {
            throw new ResourceNotFoundException("Password must be 8 character long");
        }

        if (!password.matches(patten)) {
            throw new ResourceNotFoundException("Invalid password");
        }

        return true;
    }

    public static boolean validateUUID(UUID uuid) {
        // UUID validation regex
        String pattern = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$";

        if (uuid.toString().isEmpty()) {
            throw new ResourceNotFoundException("UUID is required");
        }

        if (!uuid.toString().matches(pattern)) {
            throw new ResourceNotFoundException("Invalid UUID");
        }

        return true;
    }
}
