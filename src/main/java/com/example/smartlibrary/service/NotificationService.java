package com.example.smartlibrary.service;

import com.example.smartlibrary.model.Reservation;
import com.example.smartlibrary.model.User;

public interface NotificationService {
    void sendReservationConfirmation(Reservation reservation);
    void sendRegistrationWelcome(User user);
    void sendLoanReminder(Reservation reservation);
    void sendLoanExpired(Reservation reservation);
}

