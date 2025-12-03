package com.example.smartlibrary.repository;

import com.example.smartlibrary.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
    boolean existsByUserIdAndBookIdAndCollectedTrue(Long userId, Long bookId);
    List<Reservation> findByActiveTrue();
}
