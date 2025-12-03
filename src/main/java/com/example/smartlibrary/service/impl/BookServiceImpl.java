package com.example.smartlibrary.service.impl;

import com.example.smartlibrary.dto.BookResponse;
import com.example.smartlibrary.factory.BookFactory;
import com.example.smartlibrary.model.Book;
import com.example.smartlibrary.repository.BookRepository;
import com.example.smartlibrary.repository.ReviewRepository;
import com.example.smartlibrary.service.BookService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final ReviewRepository reviewRepository;
    private final BookFactory bookFactory;

    public BookServiceImpl(BookRepository repository,
                           ReviewRepository reviewRepository,
                           BookFactory bookFactory) {
        this.repository = repository;
        this.reviewRepository = reviewRepository;
        this.bookFactory = bookFactory;
    }

    @Override
    public List<BookResponse> search(String title, String author, String genre, Integer year) {
        return repository.search(title, author, genre, year).stream()
                .map(book -> bookFactory.toResponse(
                        book,
                        reviewRepository.averageRating(book.getId()),
                        reviewRepository.reviewsCount(book.getId())
                ))
                .toList();
    }

    @Override
    public Optional<Book> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public Book save(Book book) {
        Book sanitized = bookFactory.copyOf(book);
        return repository.save(sanitized);
    }

    @Override
    public Book update(Long id, Book book) {
        return repository.findById(id)
                .map(existing -> {
                    bookFactory.applyEditableFields(book, existing);
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public BookResponse detailed(Long id) {
        return repository.findById(id)
                .map(book -> bookFactory.toResponse(
                        book,
                        reviewRepository.averageRating(book.getId()),
                        reviewRepository.reviewsCount(book.getId())
                ))
                .orElseThrow(() -> new RuntimeException("Book not found"));
    }
}
