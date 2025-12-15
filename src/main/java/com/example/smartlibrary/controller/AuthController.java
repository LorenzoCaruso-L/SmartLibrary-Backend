package com.example.smartlibrary.controller;

import com.example.smartlibrary.dto.AuthResponse;
import com.example.smartlibrary.dto.LoginRequest;
import com.example.smartlibrary.dto.RegisterRequest;
import com.example.smartlibrary.dto.UserProfileDto;
import com.example.smartlibrary.model.User;
import com.example.smartlibrary.security.JwtService;
import com.example.smartlibrary.service.NotificationService;
import com.example.smartlibrary.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final NotificationService notificationService;

    public AuthController(UserService userService,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          NotificationService notificationService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.notificationService = notificationService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Request body required"));
        }
        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username richiesto"));
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email richiesta"));
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Password richiesta"));
        }
        
        String email = request.getEmail().trim().toLowerCase();
        if (!email.contains("@") || !email.contains(".")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Formato email non valido"));
        }
        
        if (userService.existsByUsername(request.getUsername().trim())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username già esistente"));
        }
        
        if (userService.existsByEmail(email)) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email già registrata"));
        }
        
        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setEmail(email);
        user.setPassword(request.getPassword());
        
        try {
            User saved = userService.register(user);
            notificationService.sendRegistrationWelcome(saved);
            saved.setPassword(null);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Errore durante la registrazione: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request == null || request.getUsername() == null || request.getPassword() == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Username e password richiesti"));
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );

            UserDetails principal = (UserDetails) authentication.getPrincipal();
            User user = userService.findByUsername(principal.getUsername());
            String token = jwtService.generateToken(principal);

            return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), user.getRole()));
        } catch (org.springframework.security.core.AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("error", "Credenziali non valide"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Errore durante il login: " + e.getMessage()));
        }
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
        return ResponseEntity.ok(new UserProfileDto(
                user.getId(), 
                user.getUsername(), 
                user.getEmail(), 
                user.getRole(),
                user.getProfileImageUrl()
        ));
    }
}
