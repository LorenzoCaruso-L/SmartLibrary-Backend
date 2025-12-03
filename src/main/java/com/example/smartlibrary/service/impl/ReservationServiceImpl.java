package com.example.smartlibrary.service.impl;

import com.example.smartlibrary.dto.ReservationDto;
import com.example.smartlibrary.model.Book;
import com.example.smartlibrary.model.Reservation;
import com.example.smartlibrary.model.User;
import com.example.smartlibrary.repository.BookRepository;
import com.example.smartlibrary.repository.ReservationRepository;
import com.example.smartlibrary.repository.UserRepository;
import com.example.smartlibrary.service.NotificationService;
import com.example.smartlibrary.service.ReservationService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ReservationServiceImpl implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final NotificationService notificationService;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  UserRepository userRepository,
                                  BookRepository bookRepository,
                                  NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.notificationService = notificationService;
    }

    @Override
    @Transactional
    public void reserveBook(Long bookId, String username) throws Exception {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        if (book.getCopiesAvailable() <= 0) {
            throw new Exception("No copies available");
        }

        boolean already = reservationRepository.existsByUserIdAndBookId(user.getId(), book.getId());
        if (already) throw new Exception("You already reserved this book");

        Reservation r = new Reservation();
        r.setBook(book);
        r.setUser(user);
        r.setPickupCode(UUID.randomUUID().toString().substring(0, 8));
        reservationRepository.save(r);

        book.setCopiesAvailable(book.getCopiesAvailable() - 1);
        bookRepository.save(book);

        notificationService.sendReservationConfirmation(r);
    }

    @Override
    public List<ReservationDto> reservationsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return reservationRepository.findByUserId(user.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<ReservationDto> allReservations() {
        return reservationRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void cancelReservation(Long reservationId, String username) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        if (!reservation.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Not owner");
        }
        if (!reservation.isActive()) {
            return;
        }
        reservation.setActive(false);
        reservationRepository.save(reservation);

        Book book = reservation.getBook();
        book.setCopiesAvailable(book.getCopiesAvailable() + 1);
        bookRepository.save(book);
    }

    @Override
    @Transactional
    public void markCollected(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
        reservation.setCollected(true);
        reservationRepository.save(reservation);
    }

    @Override
    public boolean userHasCollectedBook(Long bookId, Long userId) {
        return reservationRepository.existsByUserIdAndBookIdAndCollectedTrue(userId, bookId);
    }

    private ReservationDto toDto(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getBook().getId(),
                reservation.getBook().getTitle(),
                reservation.getReservationDate(),
                reservation.isActive(),
                reservation.isCollected()
        );
    }
}
