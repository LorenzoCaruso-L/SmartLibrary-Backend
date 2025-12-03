package com.example.smartlibrary.controller;

import com.example.smartlibrary.dto.AuthResponse;
import com.example.smartlibrary.dto.LoginRequest;
import com.example.smartlibrary.dto.UserProfileDto;
import com.example.smartlibrary.model.User;
import com.example.smartlibrary.security.JwtService;
import com.example.smartlibrary.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user == null || user.getUsername() == null || user.getPassword() == null) {
            return ResponseEntity.badRequest().body("username and password required");
        }
        if (userService.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("username already taken");
        }
        User saved = userService.register(user);
        saved.setPassword(null);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body("username and password required");
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        User user = userService.findByUsername(principal.getUsername());
        String token = jwtService.generateToken(principal);

        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).body("not authenticated");
        }
        User user = userService.findByUsername(principal.getName());
        if (user == null) {
            return ResponseEntity.status(404).body("user not found");
        }
        return ResponseEntity.ok(new UserProfileDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole()));
    }
}
