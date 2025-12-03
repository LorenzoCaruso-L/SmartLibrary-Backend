package com.example.smartlibrary.dto;

import java.time.LocalDate;

public class ReservationDto {
    private Long id;
    private Long bookId;
    private String bookTitle;
    private LocalDate reservationDate;
    private boolean active;
    private boolean collected;

    public ReservationDto(Long id, Long bookId, String bookTitle, LocalDate reservationDate, boolean active, boolean collected) {
        this.id = id;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.reservationDate = reservationDate;
        this.active = active;
        this.collected = collected;
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
    }
}

