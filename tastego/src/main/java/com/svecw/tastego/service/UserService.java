package com.svecw.tastego.service;

import com.svecw.tastego.model.User;
import com.svecw.tastego.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // ✅ REGISTER USER
    public User register(User user) {

        // 🔴 Validate input
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            throw new RuntimeException("Email is required");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        // 🔴 Check if email already exists
        User existing = userRepository.findByEmail(user.getEmail());
        if (existing != null) {
            throw new RuntimeException("Email already registered");
        }

        // ✅ Save user
        return userRepository.save(user);
    }

    // ✅ LOGIN USER
    public User login(String email, String password) {

        if (email == null || password == null) {
            throw new RuntimeException("Email or Password missing");
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        return user;
    }

    // ✅ UPDATE PASSWORD
    public boolean updatePassword(String email, String newPassword) {

        if (email == null || newPassword == null) {
            throw new RuntimeException("Invalid request");
        }

        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        user.setPassword(newPassword);
        userRepository.save(user);

        return true;
    }
}