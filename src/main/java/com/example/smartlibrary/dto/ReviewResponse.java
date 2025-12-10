package com.example.smartlibrary.dto;

public class ReviewResponse {
    private Long id;
    private Long bookId;
    private String username;
    private int rating;
    private String comment;

    public ReviewResponse(Long id, Long bookId, String username, int rating, String comment) {
        this.id = id;
        this.bookId = bookId;
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
