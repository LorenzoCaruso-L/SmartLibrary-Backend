package com.example.smartlibrary.service;

import com.example.smartlibrary.dto.ReviewRequest;
import com.example.smartlibrary.model.Review;

import java.util.List;

public interface ReviewService {
    Review addReview(Long bookId, String username, ReviewRequest request);
    List<Review> reviewsByBook(Long bookId);
    void deleteReview(Long reviewId);
}

