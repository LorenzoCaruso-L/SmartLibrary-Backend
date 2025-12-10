package com.example.smartlibrary.service;

import com.example.smartlibrary.dto.BookResponse;
import com.example.smartlibrary.model.Book;
import java.util.List;
import java.util.Optional;

public interface BookService {
    List<BookResponse> search(String title, String author, String genre, Integer year);
    Optional<Book> findById(Long id);
    Book save(Book book);
    Book update(Long id, Book book);
    void delete(Long id);
    void deleteAll();
    BookResponse detailed(Long id);
}
