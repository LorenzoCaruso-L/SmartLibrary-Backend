package com.example.smartlibrary.service;

import com.example.smartlibrary.dto.ReviewRequest;
import com.example.smartlibrary.dto.ReviewResponse;
import com.example.smartlibrary.model.Review;

import java.util.List;

public interface ReviewService {
    ReviewResponse addReview(Long bookId, String username, ReviewRequest request);
    List<ReviewResponse> reviewsByBook(Long bookId);
    List<ReviewResponse> getAllReviews();
    void deleteReview(Long reviewId);
    boolean canUserReview(Long bookId, String username);
    boolean hasUserReviewed(Long bookId, String username);
}

