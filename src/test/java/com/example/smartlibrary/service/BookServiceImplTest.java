package com.example.smartlibrary.service;

import com.example.smartlibrary.dto.BookResponse;
import com.example.smartlibrary.factory.BookFactory;
import com.example.smartlibrary.model.Book;
import com.example.smartlibrary.repository.BookRepository;
import com.example.smartlibrary.repository.ReviewRepository;
import com.example.smartlibrary.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private BookFactory bookFactory;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void searchAggregatesReviews() {
        Book book = new Book("Test", "Author", 2020, "Fiction", 3);
        book.setId(1L);
        when(bookRepository.search(any(), any(), any(), any())).thenReturn(List.of(book));
        when(reviewRepository.averageRating(1L)).thenReturn(4.5);
        when(reviewRepository.reviewsCount(1L)).thenReturn(2L);
        when(bookFactory.toResponse(eq(book), eq(4.5), eq(2L)))
                .thenReturn(new BookResponse(1L, "Test", "Author", 2020, "Fiction", 3, "desc", null, 4.5, 2L));

        List<BookResponse> responses = bookService.search("Test", null, null, null);

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getAverageRating()).isEqualTo(4.5);
        assertThat(responses.get(0).getReviewsCount()).isEqualTo(2);
    }

    @Test
    void detailedUsesRepository() {
        Book book = new Book("Test", "Author", 2020, "Fiction", 3);
        book.setId(2L);
        when(bookRepository.findById(2L)).thenReturn(Optional.of(book));

        when(reviewRepository.averageRating(2L)).thenReturn(null);
        when(reviewRepository.reviewsCount(2L)).thenReturn(0L);
        when(bookFactory.toResponse(eq(book), eq(null), eq(0L)))
                .thenReturn(new BookResponse(2L, "Test", "Author", 2020, "Fiction", 3, "desc", null, 0.0, 0L));

        BookResponse response = bookService.detailed(2L);

        assertThat(response.getId()).isEqualTo(2L);
        assertThat(response.getAverageRating()).isZero();
    }
}

