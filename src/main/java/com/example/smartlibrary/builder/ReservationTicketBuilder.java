package com.example.smartlibrary.builder;

import java.time.LocalDate;

public class ReservationTicketBuilder {

    private String header = "SmartLibrary - Ticket Prenotazione";
    private String pickupCode;
    private String username;
    private String bookTitle;
    private LocalDate reservationDate;
    private String instructions = "Presenta questo codice al banco prestiti entro 3 giorni.";

    public static ReservationTicketBuilder create() {
        return new ReservationTicketBuilder();
    }

    public ReservationTicketBuilder withHeader(String header) {
        this.header = header;
        return this;
    }

    public ReservationTicketBuilder withPickupCode(String pickupCode) {
        this.pickupCode = pickupCode;
        return this;
    }

    public ReservationTicketBuilder withUsername(String username) {
        this.username = username;
        return this;
    }

    public ReservationTicketBuilder withBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
        return this;
    }

    public ReservationTicketBuilder withReservationDate(LocalDate reservationDate) {
        this.reservationDate = reservationDate;
        return this;
    }

    public ReservationTicketBuilder withInstructions(String instructions) {
        this.instructions = instructions;
        return this;
    }

    public ReservationTicket build() {
        if (pickupCode == null || username == null || bookTitle == null || reservationDate == null) {
            throw new IllegalStateException("Ticket incomplete: pickupCode, username, bookTitle e reservationDate sono obbligatori");
        }
        return new ReservationTicket(header, pickupCode, username, bookTitle, reservationDate, instructions);
    }
}

