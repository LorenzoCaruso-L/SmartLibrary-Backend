package com.example.smartlibrary.service;

import com.example.smartlibrary.model.Reservation;

public interface PdfService {
    byte[] generateReservationTicket(Reservation reservation);
}

