package com.example.smartlibrary.service;

import com.example.smartlibrary.dto.ReservationDto;

import java.util.List;

public interface ReservationService {
    void reserveBook(Long bookId, String username) throws Exception;
    List<ReservationDto> reservationsForUser(String username);
    List<ReservationDto> allReservations();
    void cancelReservation(Long reservationId, String username);
    void markCollected(Long reservationId);
    boolean userHasCollectedBook(Long bookId, Long userId);
}
