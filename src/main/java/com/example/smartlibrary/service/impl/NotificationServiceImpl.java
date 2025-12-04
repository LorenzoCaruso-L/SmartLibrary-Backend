package com.example.smartlibrary.service.impl;

import com.example.smartlibrary.model.Reservation;
import com.example.smartlibrary.model.User;
import com.example.smartlibrary.service.NotificationService;
import com.example.smartlibrary.service.PdfService;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final JavaMailSender mailSender;
    private final PdfService pdfService;

    public NotificationServiceImpl(JavaMailSender mailSender, PdfService pdfService) {
        this.mailSender = mailSender;
        this.pdfService = pdfService;
    }

    @Override
    public void sendReservationConfirmation(Reservation reservation) {
        if (reservation.getUser().getEmail() == null) {
            log.warn("Email non configurata per l'utente {}", reservation.getUser().getUsername());
            return;
        }
        try {
            byte[] pdf = pdfService.generateReservationTicket(reservation);
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(reservation.getUser().getEmail());
            helper.setSubject("Conferma prenotazione libro");
            helper.setText("""
                    Ciao %s,
                    la tua prenotazione per "%s" è stata confermata. Troverai in allegato il ticket per il ritiro.
                    """.formatted(reservation.getUser().getUsername(), reservation.getBook().getTitle()), false);
            helper.addAttachment("ticket-prenotazione.pdf", () -> new ByteArrayInputStream(pdf));
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Impossibile inviare la mail di prenotazione", e);
        }
    }

    @Override
    public void sendRegistrationWelcome(User user) {
        if (user.getEmail() == null) {
            log.warn("Email non configurata per l'utente {}", user.getUsername());
            return;
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setTo(user.getEmail());
            helper.setSubject("Benvenuto in SmartLibrary");
            helper.setText("""
                    Ciao %s,

                    grazie per esserti registrato a SmartLibrary!
                    Il tuo account è stato creato con successo e ora puoi accedere per esplorare il catalogo e prenotare i tuoi libri.

                    Buona lettura,
                    Il team di SmartLibrary
                    """.formatted(user.getUsername()), false);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Impossibile inviare la mail di benvenuto per la registrazione", e);
        }
    }
}

