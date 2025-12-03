package com.example.smartlibrary.controller;

import com.example.smartlibrary.service.ReservationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;
    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }
    // ciao
    @PostMapping("/{bookId}")
    public ResponseEntity<?> reserve(@PathVariable Long bookId, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).body("login required");
        try {
            reservationService.reserveBook(bookId, principal.getName());
            return ResponseEntity.ok("Reservation created");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> myReservations(Principal principal) {
        if (principal == null) return ResponseEntity.status(401).body("login required");
        return ResponseEntity.ok(reservationService.reservationsForUser(principal.getName()));
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<?> cancel(@PathVariable Long reservationId, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).body("login required");
        reservationService.cancelReservation(reservationId, principal.getName());
        return ResponseEntity.noContent().build();
    }
}
