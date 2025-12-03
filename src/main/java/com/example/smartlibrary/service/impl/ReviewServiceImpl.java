package com.example.smartlibrary.service.impl;

import com.example.smartlibrary.dto.ReviewRequest;
import com.example.smartlibrary.model.Book;
import com.example.smartlibrary.model.Review;
import com.example.smartlibrary.model.User;
import com.example.smartlibrary.repository.BookRepository;
import com.example.smartlibrary.repository.ReviewRepository;
import com.example.smartlibrary.repository.UserRepository;
import com.example.smartlibrary.service.ReservationService;
import com.example.smartlibrary.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ReservationService reservationService;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                             UserRepository userRepository,
                             BookRepository bookRepository,
                             ReservationService reservationService) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.reservationService = reservationService;
    }

    @Override
    @Transactional
    public Review addReview(Long bookId, String username, ReviewRequest request) {
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        boolean collected = reservationService.userHasCollectedBook(bookId, user.getId());
        if (!collected) {
            throw new IllegalStateException("Book must be collected before reviewing");
        }

        if (reviewRepository.existsByBookIdAndUserId(bookId, user.getId())) {
            throw new IllegalStateException("Review already submitted");
        }

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> reviewsByBook(Long bookId) {
        return reviewRepository.findByBookId(bookId);
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }
}

