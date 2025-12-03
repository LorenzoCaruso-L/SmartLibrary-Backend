package com.example.smartlibrary.facade;

import com.example.smartlibrary.dto.ReservationDto;
import com.example.smartlibrary.factory.BookFactory;
import com.example.smartlibrary.model.Book;
import com.example.smartlibrary.service.BookService;
import com.example.smartlibrary.service.ReservationService;
import com.example.smartlibrary.service.UserService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminFacade {

    private final BookService bookService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final BookFactory bookFactory;

    public AdminFacade(BookService bookService,
                       ReservationService reservationService,
                       UserService userService,
                       BookFactory bookFactory) {
        this.bookService = bookService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.bookFactory = bookFactory;
    }

    public Book createBook(Book request) {
        Book sanitized = bookFactory.copyOf(request);
        return bookService.save(sanitized);
    }

    public Book updateBook(Long id, Book request) {
        Book sanitized = bookFactory.copyOf(request);
        return bookService.update(id, sanitized);
    }

    public void deleteBook(Long id) {
        bookService.delete(id);
    }

    public List<ReservationDto> listReservations() {
        return reservationService.allReservations();
    }

    public void markCollected(Long id) {
        reservationService.markCollected(id);
    }

    public void suspendUser(Long id) {
        userService.suspendUser(id);
    }

    public void enableUser(Long id) {
        userService.enableUser(id);
    }
}

