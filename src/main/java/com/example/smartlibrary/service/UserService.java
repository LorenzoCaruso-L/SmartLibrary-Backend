package com.example.smartlibrary.service;

import com.example.smartlibrary.model.User;

public interface UserService {
    User register(User user);
    User findByUsername(String username);
    User findByEmail(String email);
    void suspendUser(Long userId);
    void enableUser(Long userId);
    User updateProfileImage(String username, String profileImageUrl);
    void deleteAccount(String username);
}
