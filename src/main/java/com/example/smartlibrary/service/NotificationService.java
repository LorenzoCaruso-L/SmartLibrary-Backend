package com.example.smartlibrary.service;

import com.example.smartlibrary.model.Reservation;

public interface NotificationService {
    void sendReservationConfirmation(Reservation reservation);
}

