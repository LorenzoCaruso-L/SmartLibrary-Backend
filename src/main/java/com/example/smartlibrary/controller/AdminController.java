package com.example.smartlibrary.controller;

import com.example.smartlibrary.facade.AdminFacade;
import com.example.smartlibrary.model.Book;
import com.example.smartlibrary.service.BookService;
import com.example.smartlibrary.service.GoogleBooksService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final AdminFacade adminFacade;
    private final GoogleBooksService googleBooksService;
    private final BookService bookService;

    public AdminController(AdminFacade adminFacade, 
                          GoogleBooksService googleBooksService,
                          BookService bookService) {
        this.adminFacade = adminFacade;
        this.googleBooksService = googleBooksService;
        this.bookService = bookService;
    }

    @PostMapping("/books")
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        return ResponseEntity.ok(adminFacade.createBook(book));
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id, @RequestBody Book book) {
        return ResponseEntity.ok(adminFacade.updateBook(id, book));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable Long id) {
        adminFacade.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/reservations")
    public ResponseEntity<?> reservations() {
        return ResponseEntity.ok(adminFacade.listReservations());
    }

    @PostMapping("/reservations/{id}/collected")
    public ResponseEntity<?> markCollected(@PathVariable Long id) {
        adminFacade.markCollected(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/suspend")
    public ResponseEntity<?> suspend(@PathVariable Long id) {
        adminFacade.suspendUser(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{id}/enable")
    public ResponseEntity<?> enable(@PathVariable Long id) {
        adminFacade.enableUser(id);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Cerca libri su Google Books
     * GET /admin/books/search?q=thriller&maxResults=10
     */
    @GetMapping("/books/search")
    public ResponseEntity<?> searchGoogleBooks(
            @RequestParam String q,
            @RequestParam(defaultValue = "10") int maxResults) {
        List<Book> books = googleBooksService.searchAndConvert(q, maxResults);
        return ResponseEntity.ok(books);
    }
    
    /**
     * Importa libri da Google Books nel database
     * POST /admin/books/import
     * Body: { "query": "thriller", "maxResults": 20 }
     */
    @PostMapping("/books/import")
    public ResponseEntity<?> importBooks(@RequestBody Map<String, Object> request) {
        String query = (String) request.get("query");
        int maxResults = request.containsKey("maxResults") 
            ? ((Number) request.get("maxResults")).intValue() 
            : 10;
        
        if (query == null || query.isEmpty()) {
            return ResponseEntity.badRequest().body("Query parameter 'query' is required");
        }
        
        List<Book> foundBooks = googleBooksService.searchAndConvert(query, maxResults);
        List<Book> savedBooks = new java.util.ArrayList<>();
        
        for (Book book : foundBooks) {
            try {
                Book saved = bookService.save(book);
                savedBooks.add(saved);
            } catch (Exception e) {
                System.err.println("Errore nel salvataggio libro: " + book.getTitle() + " - " + e.getMessage());
            }
        }
        
        return ResponseEntity.ok(Map.of(
            "imported", savedBooks.size(),
            "totalFound", foundBooks.size(),
            "books", savedBooks
        ));
    }
}

