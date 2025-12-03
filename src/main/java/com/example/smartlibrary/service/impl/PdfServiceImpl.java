package com.example.smartlibrary.service.impl;

import com.example.smartlibrary.builder.ReservationTicket;
import com.example.smartlibrary.builder.ReservationTicketBuilder;
import com.example.smartlibrary.model.Reservation;
import com.example.smartlibrary.service.PdfService;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfServiceImpl implements PdfService {

    @Override
    public byte[] generateReservationTicket(Reservation reservation) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ReservationTicket ticket = ReservationTicketBuilder.create()
                    .withPickupCode(reservation.getPickupCode())
                    .withUsername(reservation.getUser().getUsername())
                    .withBookTitle(reservation.getBook().getTitle())
                    .withReservationDate(reservation.getReservationDate())
                    .build();

            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();
            document.add(new Paragraph(ticket.getHeader()));
            document.add(new Paragraph("Codice ritiro: " + ticket.getPickupCode()));
            document.add(new Paragraph("Utente: " + ticket.getUsername()));
            document.add(new Paragraph("Libro: " + ticket.getBookTitle()));
            document.add(new Paragraph("Data prenotazione: " + ticket.getReservationDate()));
            document.add(new Paragraph(ticket.getInstructions()));
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Errore nella generazione del PDF", e);
        }
    }
}

