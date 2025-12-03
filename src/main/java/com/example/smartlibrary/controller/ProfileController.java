package com.example.smartlibrary.controller;

import com.example.smartlibrary.dto.UserProfileDto;
import com.example.smartlibrary.model.User;
import com.example.smartlibrary.service.ReservationService;
import com.example.smartlibrary.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;
    private final ReservationService reservationService;

    public ProfileController(UserService userService, ReservationService reservationService) {
        this.userService = userService;
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<?> profile(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).body("login required");
        User user = userService.findByUsername(principal.getName());
        if (user == null) return ResponseEntity.status(404).body("user not found");
        return ResponseEntity.ok(new UserProfileDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole()));
    }

    @GetMapping("/reservations")
    public ResponseEntity<?> reservations(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).body("login required");
        return ResponseEntity.ok(reservationService.reservationsForUser(principal.getName()));
    }
}

