package com.example.smartlibrary.controller;

import com.example.smartlibrary.dto.BookResponse;
import com.example.smartlibrary.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
@CrossOrigin("*") // permette il collegamento del frontend
public class BookController {

    private final BookService service;

    public BookController(BookService service) {
        this.service = service;
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
}
