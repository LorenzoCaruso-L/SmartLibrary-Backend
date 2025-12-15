package com.example.smartlibrary.repository;

import com.example.smartlibrary.model.Reservation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
    boolean existsByUserIdAndBookId(Long userId, Long bookId);
    boolean existsByUserIdAndBookIdAndCollectedTrue(Long userId, Long bookId);
    List<Reservation> findByActiveTrue();
    boolean existsByBookIdAndCollectedTrue(Long bookId);
    
    @EntityGraph(attributePaths = {"user", "book"})
    @Query("SELECT r FROM Reservation r WHERE r.collected = true AND r.dueDate IS NOT NULL")
    List<Reservation> findByCollectedTrueAndDueDateIsNotNull();
}
