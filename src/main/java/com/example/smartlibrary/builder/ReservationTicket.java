package com.example.smartlibrary.builder;

import java.time.LocalDate;

public class ReservationTicket {
    private final String header;
    private final String pickupCode;
    private final String username;
    private final String bookTitle;
    private final LocalDate reservationDate;
    private final String instructions;

    ReservationTicket(String header,
                      String pickupCode,
                      String username,
                      String bookTitle,
                      LocalDate reservationDate,
                      String instructions) {
        this.header = header;
        this.pickupCode = pickupCode;
        this.username = username;
        this.bookTitle = bookTitle;
        this.reservationDate = reservationDate;
        this.instructions = instructions;
    }

    public String getHeader() {
        return header;
    }

    public String getPickupCode() {
        return pickupCode;
    }

    public String getUsername() {
        return username;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDate getReservationDate() {
        return reservationDate;
    }

    public String getInstructions() {
        return instructions;
    }
}

