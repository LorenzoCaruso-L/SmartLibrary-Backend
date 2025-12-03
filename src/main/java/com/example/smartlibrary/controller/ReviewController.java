package com.example.smartlibrary.controller;

import com.example.smartlibrary.dto.ReviewRequest;
import com.example.smartlibrary.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/{bookId}")
    public ResponseEntity<?> addReview(@PathVariable Long bookId, @RequestBody ReviewRequest request, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).body("login required");
        try {
            return ResponseEntity.ok(reviewService.addReview(bookId, principal.getName(), request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<?> getReviewsByBook(@PathVariable Long bookId) {
        return ResponseEntity.ok(reviewService.reviewsByBook(bookId));
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}
