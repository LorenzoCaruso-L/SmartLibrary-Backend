package com.example.smartlibrary.controller;

import com.example.smartlibrary.dto.BookResponse;
import com.example.smartlibrary.dto.ReviewRequest;
import com.example.smartlibrary.service.BookService;
import com.example.smartlibrary.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin("*")
public class BookController {

    private final BookService service;
    private final ReviewService reviewService;

    public BookController(BookService service, ReviewService reviewService) {
        this.service = service;
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<BookResponse> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer year
    ) {
        return service.search(title, author, genre, year);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.detailed(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<?> getBookReviews(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.reviewsByBook(id));
    }

    @PostMapping("/{id}/reviews")
    public ResponseEntity<?> addBookReview(@PathVariable Long id, @RequestBody ReviewRequest request, Principal principal) {
        if (principal == null) return ResponseEntity.status(401).body("login required");
        try {
            return ResponseEntity.ok(reviewService.addReview(id, principal.getName(), request));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
