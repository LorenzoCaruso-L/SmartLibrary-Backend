package com.example.smartlibrary.service;

import com.example.smartlibrary.dto.ReservationDto;
import com.example.smartlibrary.model.Book;
import com.example.smartlibrary.model.Reservation;
import com.example.smartlibrary.model.User;
import com.example.smartlibrary.repository.BookRepository;
import com.example.smartlibrary.repository.ReservationRepository;
import com.example.smartlibrary.repository.UserRepository;
import com.example.smartlibrary.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private User user;
    private Book book;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("mario");

        book = new Book("Test", "Author", 2020, "Fiction", 2);
        book.setId(10L);
    }

    @Test
    void reserveBookFailsWhenAlreadyReserved() {
        when(userRepository.findByUsername("mario")).thenReturn(Optional.of(user));
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(reservationRepository.existsByUserIdAndBookId(1L, 10L)).thenReturn(true);

        assertThatThrownBy(() -> reservationService.reserveBook(10L, "mario"))
                .isInstanceOf(Exception.class)
                .hasMessageContaining("already reserved");
    }

    @Test
    void reservationsForUserMapsDto() {
        Reservation reservation = new Reservation();
        reservation.setId(5L);
        reservation.setUser(user);
        reservation.setBook(book);
        when(userRepository.findByUsername("mario")).thenReturn(Optional.of(user));
        when(reservationRepository.findByUserId(1L)).thenReturn(List.of(reservation));

        List<ReservationDto> dtos = reservationService.reservationsForUser("mario");

        assertThat(dtos).hasSize(1);
        assertThat(dtos.get(0).getBookId()).isEqualTo(10L);
    }
}

