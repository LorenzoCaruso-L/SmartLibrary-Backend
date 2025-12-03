package com.example.smartlibrary.service.impl;

import com.example.smartlibrary.model.User;
import com.example.smartlibrary.repository.UserRepository;
import com.example.smartlibrary.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) user.setRole("ROLE_USER");
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public void suspendUser(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setEnabled(false);
            user.setLocked(true);
            userRepository.save(user);
        });
    }

    @Override
    public void enableUser(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setEnabled(true);
            user.setLocked(false);
            userRepository.save(user);
        });
    }
}
