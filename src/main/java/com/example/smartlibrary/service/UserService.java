package com.example.smartlibrary.service;

import com.example.smartlibrary.model.User;

public interface UserService {
    User register(User user);
    User findByUsername(String username);
    void suspendUser(Long userId);
    void enableUser(Long userId);
}
