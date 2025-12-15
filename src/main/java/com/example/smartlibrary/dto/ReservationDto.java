package com.example.smartlibrary.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReservationDto {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private Integer bookPublicationYear;
    private String bookGenre;
    private String bookDescription;
    private String bookCoverImageUrl;
    private LocalDate reservationDate;
    private boolean active;
    private boolean collected;
    private LocalDateTime dueDate;
    private boolean expired;

    public ReservationDto(Long id, Long bookId, String bookTitle, LocalDate reservationDate, boolean active, boolean collected) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.reservationDate = reservationDate;
        this.active = active;
        this.collected = collected;
        this.dueDate = null;
        this.expired = false;
    }

    public ReservationDto(Long id, Long bookId, String bookTitle, String bookAuthor, Integer bookPublicationYear, 
                         String bookGenre, String bookDescription, String bookCoverImageUrl, 
                         LocalDate reservationDate, boolean active, boolean collected) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPublicationYear = bookPublicationYear;
        this.bookGenre = bookGenre;
        this.bookDescription = bookDescription;
        this.bookCoverImageUrl = bookCoverImageUrl;
        this.reservationDate = reservationDate;
        this.active = active;
        this.collected = collected;
        this.dueDate = null;
        this.expired = false;
    }

    public ReservationDto(Long id, Long bookId, String bookTitle, String bookAuthor, Integer bookPublicationYear, 
                         String bookGenre, String bookDescription, String bookCoverImageUrl, 
                         LocalDate reservationDate, boolean active, boolean collected, LocalDateTime dueDate) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPublicationYear = bookPublicationYear;
        this.bookGenre = bookGenre;
        this.bookDescription = bookDescription;
        this.bookCoverImageUrl = bookCoverImageUrl;
        this.reservationDate = reservationDate;
        this.active = active;
        this.collected = collected;
        this.dueDate = dueDate;
        this.expired = collected && dueDate != null && dueDate.isBefore(LocalDateTime.now());
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

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isCollected() {
        return collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
        this.expired = collected && dueDate != null && dueDate.isBefore(LocalDateTime.now());
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public Integer getBookPublicationYear() {
        return bookPublicationYear;
    }

    public void setBookPublicationYear(Integer bookPublicationYear) {
        this.bookPublicationYear = bookPublicationYear;
    }

    public String getBookGenre() {
        return bookGenre;
    }

    public void setBookGenre(String bookGenre) {
        this.bookGenre = bookGenre;
    }

    public String getBookDescription() {
        return bookDescription;
    }

    public void setBookDescription(String bookDescription) {
        this.bookDescription = bookDescription;
    }

    public String getBookCoverImageUrl() {
        return bookCoverImageUrl;
    }

    public void setBookCoverImageUrl(String bookCoverImageUrl) {
        this.bookCoverImageUrl = bookCoverImageUrl;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
        this.expired = collected && dueDate != null && dueDate.isBefore(LocalDateTime.now());
    }

    public boolean isExpired() {
        return expired;
    }

    public void setExpired(boolean expired) {
        this.expired = expired;
    }
}

