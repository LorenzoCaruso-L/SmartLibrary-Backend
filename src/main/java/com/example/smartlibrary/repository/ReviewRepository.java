package com.example.smartlibrary.repository;

import com.example.smartlibrary.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByBookId(Long bookId);

    boolean existsByBookIdAndUserId(Long bookId, Long userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.book.id = :bookId")
    Double averageRating(@Param("bookId") Long bookId);

    @Query("SELECT COUNT(r.id) FROM Review r WHERE r.book.id = :bookId")
    Long reviewsCount(@Param("bookId") Long bookId);
}
