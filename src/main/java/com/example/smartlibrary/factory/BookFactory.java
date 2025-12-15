package com.example.smartlibrary.factory;

import com.example.smartlibrary.dto.BookResponse;
import com.example.smartlibrary.model.Book;
import org.springframework.stereotype.Component;

@Component
public class BookFactory {

    public Book createBook(String title,
                           String author,
                           Integer year,
                           String genre,
                           Integer copiesAvailable,
                           String description) {
        return createBook(title, author, year, genre, copiesAvailable, description, null);
    }
    
    public Book createBook(String title,
                           String author,
                           Integer year,
                           String genre,
                           Integer copiesAvailable,
                           String description,
                           String coverImageUrl) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setPublicationYear(year);
        book.setGenre(genre);
        book.setCopiesAvailable(copiesAvailable);
        book.setDescription(description);
        book.setCoverImageUrl(coverImageUrl);
        return book;
    }


    public Book copyOf(Book source) {
        if (source == null) return null;
        return createBook(
                source.getTitle(),
                source.getAuthor(),
                source.getPublicationYear(),
                source.getGenre(),
                source.getCopiesAvailable(),
                source.getDescription(),
                source.getCoverImageUrl()
        );
    }


    public void applyEditableFields(Book source, Book target) {
        if (source == null || target == null) return;
        target.setTitle(source.getTitle());
        target.setAuthor(source.getAuthor());
        target.setGenre(source.getGenre());
        target.setPublicationYear(source.getPublicationYear());
        target.setCopiesAvailable(source.getCopiesAvailable());
        target.setDescription(source.getDescription());
        target.setCoverImageUrl(source.getCoverImageUrl());
    }

    public BookResponse toResponse(Book book, Double averageRating, Long reviewsCount) {
        double rating = averageRating != null ? Math.round(averageRating * 10.0) / 10.0 : 0.0;
        long totalReviews = reviewsCount != null ? reviewsCount : 0L;
        return new BookResponse(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getPublicationYear(),
                book.getGenre(),
                book.getCopiesAvailable(),
                book.getDescription(),
                book.getCoverImageUrl(),
                rating,
                totalReviews
        );
    }
}

