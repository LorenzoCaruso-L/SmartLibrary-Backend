package com.example.smartlibrary.controller;

import com.example.smartlibrary.dto.UserProfileDto;
import com.example.smartlibrary.model.User;
import com.example.smartlibrary.service.ReservationService;
import com.example.smartlibrary.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        return ResponseEntity.ok(new UserProfileDto(
                user.getId(), 
                user.getUsername(), 
                user.getEmail(), 
                user.getRole(),
                user.getProfileImageUrl()
        ));
    }

    @PostMapping("/image")
    public ResponseEntity<?> updateProfileImage(Principal principal, @RequestBody java.util.Map<String, String> request) {
        if (principal == null) return ResponseEntity.status(401).body("login required");
        String imageUrl = request.get("profileImageUrl");
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("profileImageUrl is required");
        }
        User updated = userService.updateProfileImage(principal.getName(), imageUrl);
        return ResponseEntity.ok(new UserProfileDto(
                updated.getId(),
                updated.getUsername(),
                updated.getEmail(),
                updated.getRole(),
                updated.getProfileImageUrl()
        ));
    }

    @GetMapping("/reservations")
    public ResponseEntity<?> reservations(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).body("login required");
        return ResponseEntity.ok(reservationService.reservationsForUser(principal.getName()));
    }


    @DeleteMapping({"/account", "/delete"})
    public ResponseEntity<?> deleteAccount(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).body("login required");
        try {
            userService.deleteAccount(principal.getName());
            return ResponseEntity.ok(java.util.Map.of(
                "message", "Account eliminato con successo",
                "deleted", true
            ));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(java.util.Map.of(
                "error", "Utente non trovato",
                "message", e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(java.util.Map.of(
                "error", "Errore durante l'eliminazione dell'account",
                "message", e.getMessage()
            ));
        }
    }
}

