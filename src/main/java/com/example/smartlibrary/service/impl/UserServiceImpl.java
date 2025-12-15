package com.example.smartlibrary.service.impl;

import com.example.smartlibrary.model.Reservation;
import com.example.smartlibrary.model.Review;
import com.example.smartlibrary.model.User;
import com.example.smartlibrary.repository.BookRepository;
import com.example.smartlibrary.repository.ReservationRepository;
import com.example.smartlibrary.repository.ReviewRepository;
import com.example.smartlibrary.repository.UserRepository;
import com.example.smartlibrary.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ReservationRepository reservationRepository;
    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public UserServiceImpl(UserRepository userRepository, 
                          PasswordEncoder passwordEncoder,
                          ReservationRepository reservationRepository,
                          ReviewRepository reviewRepository,
                          BookRepository bookRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.reservationRepository = reservationRepository;
        this.reviewRepository = reviewRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public User register(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRole() == null) user.setRole("ROLE_USER");
        return userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElse(null);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public void suspendUser(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setEnabled(false);
            user.setLocked(true);
            userRepository.save(user);
        });
    }

    @Override
    public void enableUser(Long userId) {
        userRepository.findById(userId).ifPresent(user -> {
            user.setEnabled(true);
            user.setLocked(false);
            userRepository.save(user);
        });
    }

    @Override
    public User updateProfileImage(String username, String profileImageUrl) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setProfileImageUrl(profileImageUrl);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteAccount(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Long userId = user.getId();

        // 1. Elimina tutte le prenotazioni dell'utente e ripristina le copie disponibili
        java.util.List<Reservation> reservations = reservationRepository.findByUserId(userId);
        for (Reservation reservation : reservations) {
            // Ripristina le copie disponibili se la prenotazione era attiva
            if (reservation.isActive()) {
                var book = reservation.getBook();
                book.setCopiesAvailable(book.getCopiesAvailable() + 1);
                bookRepository.save(book); // Salva il libro con copie aggiornate
            }
        }
        reservationRepository.deleteAll(reservations);

        // 2. Elimina tutte le recensioni dell'utente
        java.util.List<Review> reviews = reviewRepository.findByUserId(userId);
        reviewRepository.deleteAll(reviews);

        // 3. Elimina l'utente
        userRepository.delete(user);
    }
}
