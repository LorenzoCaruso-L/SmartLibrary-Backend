package com.example.smartlibrary.service.impl;

import com.example.smartlibrary.dto.ReviewRequest;
import com.example.smartlibrary.dto.ReviewResponse;
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
    public ReviewResponse addReview(Long bookId, String username, ReviewRequest request) {
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Verifica che l'utente abbia prenotato il libro (non necessariamente raccolto)
        boolean reserved = reservationService.userHasReservedBook(bookId, user.getId());
        if (!reserved) {
            throw new IllegalStateException("You must reserve the book before reviewing");
        }

        if (reviewRepository.existsByBookIdAndUserId(bookId, user.getId())) {
            throw new IllegalStateException("Review already submitted");
        }

        Review review = new Review();
        review.setUser(user);
        review.setBook(book);
        review.setRating(request.getRating());
        review.setComment(request.getComment() != null ? request.getComment() : "");
        Review saved = reviewRepository.save(review);
        
        return new ReviewResponse(
            saved.getId(),
            saved.getBook().getId(),
            saved.getUser().getUsername(),
            saved.getRating(),
            saved.getComment()
        );
    }

    @Override
    public List<ReviewResponse> reviewsByBook(Long bookId) {
        return reviewRepository.findByBookId(bookId).stream()
                .map(review -> new ReviewResponse(
                    review.getId(),
                    review.getBook().getId(),
                    review.getUser().getUsername(),
                    review.getRating(),
                    review.getComment() != null ? review.getComment() : ""
                ))
                .toList();
    }

    @Override
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(review -> new ReviewResponse(
                    review.getId(),
                    review.getBook().getId(),
                    review.getUser().getUsername(),
                    review.getRating(),
                    review.getComment() != null ? review.getComment() : ""
                ))
                .toList();
    }

    @Override
    public void deleteReview(Long reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public boolean canUserReview(Long bookId, String username) {
        User user = userRepository.findByUsername(username)
                .orElse(null);
        if (user == null) return false;
        
        // Verifica che l'utente abbia prenotato il libro
        boolean reserved = reservationService.userHasReservedBook(bookId, user.getId());
        if (!reserved) return false;
        
        // Verifica che l'utente non abbia già recensito il libro
        boolean alreadyReviewed = reviewRepository.existsByBookIdAndUserId(bookId, user.getId());
        return !alreadyReviewed;
    }

    @Override
    public boolean hasUserReviewed(Long bookId, String username) {
        User user = userRepository.findByUsername(username)
                .orElse(null);
        if (user == null) return false;
        return reviewRepository.existsByBookIdAndUserId(bookId, user.getId());
    }
}

