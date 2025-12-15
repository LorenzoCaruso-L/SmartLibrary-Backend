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
            helper.setFrom("smartlibrary.ingsw@gmail.com", "SmartLibrary");
            helper.setTo(reservation.getUser().getEmail());
            helper.setSubject("📚 Conferma prenotazione - " + reservation.getBook().getTitle());
            String htmlContent = buildReservationEmailHtml(
                    reservation.getUser().getUsername(),
                    reservation.getBook().getTitle(),
                    reservation.getBook().getAuthor(),
                    reservation.getPickupCode(),
                    reservation.getReservationDate().toString()
            );
            helper.setText(htmlContent, true);
            helper.addAttachment("ticket-prenotazione.pdf", () -> new ByteArrayInputStream(pdf));
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Impossibile inviare la mail di prenotazione a: {}", reservation.getUser().getEmail(), e);
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
            helper.setFrom("smartlibrary.ingsw@gmail.com", "SmartLibrary");
            helper.setTo(user.getEmail());
            helper.setSubject("🎉 Benvenuto in SmartLibrary!");
            String htmlContent = buildWelcomeEmailHtml(user.getUsername());
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Impossibile inviare la mail di benvenuto per la registrazione a: {}", user.getEmail(), e);
        }
    }

    private String buildWelcomeEmailHtml(String username) {
        return """
            <!DOCTYPE html>
            <html lang="it">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 20px;
                        background-color: #f4f4f4;
                    }
                    .container {
                        background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                        border-radius: 10px;
                        padding: 40px;
                        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                    }
                    .content {
                        background: white;
                        border-radius: 8px;
                        padding: 40px;
                        margin-top: 20px;
                    }
                    .header {
                        text-align: center;
                        color: white;
                        margin-bottom: 30px;
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 32px;
                        font-weight: 700;
                    }
                    .header .icon {
                        font-size: 64px;
                        margin-bottom: 10px;
                    }
                    .welcome-text {
                        font-size: 18px;
                        color: #555;
                        margin-bottom: 20px;
                        text-align: center;
                    }
                    .highlight {
                        color: #667eea;
                        font-weight: 600;
                    }
                    .features {
                        background: #f8f9fa;
                        border-radius: 8px;
                        padding: 25px;
                        margin: 30px 0;
                    }
                    .feature-item {
                        display: flex;
                        align-items: center;
                        margin-bottom: 15px;
                        font-size: 16px;
                    }
                    .feature-item:last-child {
                        margin-bottom: 0;
                    }
                    .feature-icon {
                        font-size: 24px;
                        margin-right: 15px;
                        width: 30px;
                        text-align: center;
                    }
                    .footer {
                        text-align: center;
                        margin-top: 30px;
                        padding-top: 20px;
                        border-top: 2px solid #eee;
                        color: #777;
                        font-size: 14px;
                    }
                    .footer .team {
                        color: #667eea;
                        font-weight: 600;
                        margin-top: 10px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="icon">📚</div>
                        <h1>SmartLibrary</h1>
                    </div>
                    <div class="content">
                        <div class="welcome-text">
                            <h2 style="color: #667eea; margin-top: 0;">Ciao <span class="highlight">%s</span>! 👋</h2>
                            <p>Grazie per esserti registrato a <strong>SmartLibrary</strong>!</p>
                            <p>Il tuo account è stato creato con successo e ora puoi iniziare la tua avventura letteraria.</p>
                        </div>
                        
                        <div class="features">
                            <div class="feature-item">
                                <div class="feature-icon">🔍</div>
                                <div>Esplora il nostro vasto catalogo di libri</div>
                            </div>
                            <div class="feature-item">
                                <div class="feature-icon">📖</div>
                                <div>Prenota i libri che ti interessano</div>
                            </div>
                            <div class="feature-item">
                                <div class="feature-icon">⭐</div>
                                <div>Lascia recensioni e condividi le tue opinioni</div>
                            </div>
                            <div class="feature-item">
                                <div class="feature-icon">📧</div>
                                <div>Ricevi notifiche e aggiornamenti via email</div>
                            </div>
                        </div>
                        
                        <div style="text-align: center; margin-top: 30px;">
                            <p style="font-size: 16px; color: #555;">Siamo felici di averti nella nostra community di lettori!</p>
                        </div>
                        
                        <div class="footer">
                            <p>Buona lettura! 📖</p>
                            <p class="team">Il team di SmartLibrary</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(username);
    }

    private String buildReservationEmailHtml(String username, String bookTitle, String bookAuthor, 
                                            String pickupCode, String reservationDate) {
        return """
            <!DOCTYPE html>
            <html lang="it">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 20px;
                        background-color: #f4f4f4;
                    }
                    .container {
                        background: linear-gradient(135deg, #667eea 0%%, #764ba2 100%%);
                        border-radius: 10px;
                        padding: 40px;
                        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                    }
                    .content {
                        background: white;
                        border-radius: 8px;
                        padding: 40px;
                        margin-top: 20px;
                    }
                    .header {
                        text-align: center;
                        color: white;
                        margin-bottom: 30px;
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 28px;
                        font-weight: 700;
                    }
                    .header .icon {
                        font-size: 56px;
                        margin-bottom: 10px;
                    }
                    .success-badge {
                        background: #28a745;
                        color: white;
                        padding: 12px 24px;
                        border-radius: 25px;
                        display: inline-block;
                        font-weight: 600;
                        margin: 20px 0;
                        font-size: 16px;
                    }
                    .book-info {
                        background: linear-gradient(135deg, #f8f9fa 0%%, #e9ecef 100%%);
                        border-left: 4px solid #667eea;
                        padding: 25px;
                        border-radius: 8px;
                        margin: 25px 0;
                    }
                    .book-title {
                        font-size: 22px;
                        font-weight: 700;
                        color: #667eea;
                        margin: 0 0 10px 0;
                    }
                    .book-author {
                        font-size: 16px;
                        color: #666;
                        margin: 0;
                    }
                    .pickup-section {
                        background: #fff3cd;
                        border: 2px solid #ffc107;
                        border-radius: 8px;
                        padding: 25px;
                        margin: 25px 0;
                        text-align: center;
                    }
                    .pickup-section h3 {
                        color: #856404;
                        margin-top: 0;
                        font-size: 20px;
                    }
                    .pickup-code {
                        background: white;
                        border: 2px dashed #ffc107;
                        padding: 15px;
                        border-radius: 8px;
                        font-size: 24px;
                        font-weight: 700;
                        color: #856404;
                        letter-spacing: 2px;
                        margin: 15px 0;
                        font-family: 'Courier New', monospace;
                    }
                    .details {
                        background: #f8f9fa;
                        border-radius: 8px;
                        padding: 20px;
                        margin: 25px 0;
                    }
                    .detail-row {
                        display: flex;
                        justify-content: space-between;
                        padding: 12px 0;
                        border-bottom: 1px solid #dee2e6;
                    }
                    .detail-row:last-child {
                        border-bottom: none;
                    }
                    .detail-label {
                        font-weight: 600;
                        color: #666;
                    }
                    .detail-value {
                        color: #333;
                        font-weight: 500;
                    }
                    .info-box {
                        background: #d1ecf1;
                        border-left: 4px solid #0c5460;
                        padding: 15px;
                        border-radius: 4px;
                        margin: 20px 0;
                        color: #0c5460;
                    }
                    .pdf-notice {
                        background: #e7f3ff;
                        border-left: 4px solid #0066cc;
                        padding: 15px;
                        border-radius: 4px;
                        margin: 20px 0;
                        color: #0066cc;
                        text-align: center;
                    }
                    .footer {
                        text-align: center;
                        margin-top: 30px;
                        padding-top: 20px;
                        border-top: 2px solid #eee;
                        color: #777;
                        font-size: 14px;
                    }
                    .footer .team {
                        color: #667eea;
                        font-weight: 600;
                        margin-top: 10px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="icon">📚</div>
                        <h1>Prenotazione Confermata!</h1>
                    </div>
                    <div class="content">
                        <div style="text-align: center;">
                            <div class="success-badge">✓ Prenotazione completata con successo</div>
                        </div>
                        
                        <p style="font-size: 18px; text-align: center; color: #555;">
                            Ciao <strong style="color: #667eea;">%s</strong>,<br>
                            grazie per la tua prenotazione!
                        </p>
                        
                        <div class="book-info">
                            <p class="book-title">"%s"</p>
                            <p class="book-author">di %s</p>
                        </div>
                        
                        <div class="pickup-section">
                            <h3>🎯 INVITO AL RITIRO</h3>
                            <p style="color: #856404; margin-bottom: 10px;">
                                Ti invitiamo a recarti presso la biblioteca per ritirare il libro prenotato.
                            </p>
                            <div class="pickup-code">%s</div>
                            <p style="color: #856404; font-size: 14px; margin-top: 10px;">
                                Presenta questo codice al banco prestiti
                            </p>
                        </div>
                        
                        <div class="details">
                            <div class="detail-row">
                                <span class="detail-label">📅 Data prenotazione:</span>
                                <span class="detail-value">%s</span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label">📖 Titolo:</span>
                                <span class="detail-value">%s</span>
                            </div>
                            <div class="detail-row">
                                <span class="detail-label">✍️ Autore:</span>
                                <span class="detail-value">%s</span>
                            </div>
                        </div>
                        
                        <div class="pdf-notice">
                            <strong>📎 Allegato PDF</strong><br>
                            Ricorda di portare con te il ticket PDF allegato a questa email.
                        </div>
                        
                        <div class="info-box">
                            <strong>⏰ Importante:</strong><br>
                            Il libro sarà disponibile per il ritiro entro <strong>3 giorni</strong> dalla data di prenotazione.
                        </div>
                        
                        <div class="footer">
                            <p>A presto! 📚</p>
                            <p class="team">Il team di SmartLibrary</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(username, bookTitle, bookAuthor, pickupCode, reservationDate, bookTitle, bookAuthor);
    }

    @Override
    public void sendLoanReminder(Reservation reservation) {
        if (reservation.getUser().getEmail() == null) {
            return;
        }
        
        String userEmail = reservation.getUser().getEmail();
        if (userEmail.endsWith("@fake.com")) {
            return;
        }
        
        if (reservation.getDueDate() == null) {
            return;
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setFrom("smartlibrary.ingsw@gmail.com", "SmartLibrary");
            helper.setTo(reservation.getUser().getEmail());
            helper.setSubject("⏰ Promemoria: Restituisci il libro entro 3 giorni - " + reservation.getBook().getTitle());
            String htmlContent = buildLoanReminderEmailHtml(
                    reservation.getUser().getUsername(),
                    reservation.getBook().getTitle(),
                    reservation.getBook().getAuthor(),
                    reservation.getDueDate()
            );
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Impossibile inviare la mail di promemoria prestito a: {}", reservation.getUser().getEmail(), e);
        }
    }

    @Override
    public void sendLoanExpired(Reservation reservation) {
        if (reservation.getUser().getEmail() == null) {
            return;
        }
        
        String userEmail = reservation.getUser().getEmail();
        if (userEmail.endsWith("@fake.com")) {
            return;
        }
        
        if (reservation.getDueDate() == null) {
            return;
        }
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setFrom("smartlibrary.ingsw@gmail.com", "SmartLibrary");
            helper.setTo(reservation.getUser().getEmail());
            helper.setSubject("🚨 IL TEMPO È SCADUTO - Restituisci immediatamente il libro!");
            String htmlContent = buildLoanExpiredEmailHtml(
                    reservation.getUser().getUsername(),
                    reservation.getBook().getTitle(),
                    reservation.getBook().getAuthor(),
                    reservation.getDueDate()
            );
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Impossibile inviare la mail di scadenza prestito a: {}", reservation.getUser().getEmail(), e);
        }
    }

    private String buildLoanReminderEmailHtml(String username, String bookTitle, String bookAuthor, 
                                             java.time.LocalDateTime dueDate) {
        return """
            <!DOCTYPE html>
            <html lang="it">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 20px;
                        background-color: #f4f4f4;
                    }
                    .container {
                        background: linear-gradient(135deg, #ffc107 0%%, #ff9800 100%%);
                        border-radius: 10px;
                        padding: 40px;
                        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                    }
                    .content {
                        background: white;
                        border-radius: 8px;
                        padding: 40px;
                        margin-top: 20px;
                    }
                    .header {
                        text-align: center;
                        color: white;
                        margin-bottom: 30px;
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 28px;
                        font-weight: 700;
                    }
                    .reminder-badge {
                        background: #ff9800;
                        color: white;
                        padding: 12px 24px;
                        border-radius: 25px;
                        display: inline-block;
                        font-weight: 600;
                        margin: 20px 0;
                        font-size: 16px;
                    }
                    .book-info {
                        background: linear-gradient(135deg, #fff3cd 0%%, #ffe69c 100%%);
                        border-left: 4px solid #ff9800;
                        padding: 25px;
                        border-radius: 8px;
                        margin: 25px 0;
                    }
                    .book-title {
                        font-size: 22px;
                        font-weight: 700;
                        color: #856404;
                        margin: 0 0 10px 0;
                    }
                    .book-author {
                        font-size: 16px;
                        color: #856404;
                        margin: 0;
                    }
                    .warning-box {
                        background: #fff3cd;
                        border: 2px solid #ffc107;
                        border-radius: 8px;
                        padding: 25px;
                        margin: 25px 0;
                        text-align: center;
                    }
                    .warning-box h3 {
                        color: #856404;
                        margin-top: 0;
                        font-size: 20px;
                    }
                    .footer {
                        text-align: center;
                        margin-top: 30px;
                        padding-top: 20px;
                        border-top: 2px solid #eee;
                        color: #777;
                        font-size: 14px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>⏰ Promemoria Restituzione</h1>
                    </div>
                    <div class="content">
                        <div style="text-align: center;">
                            <div class="reminder-badge">📅 Restituisci entro 3 giorni</div>
                        </div>
                        
                        <p style="font-size: 18px; text-align: center; color: #555;">
                            Ciao <strong style="color: #ff9800;">%s</strong>,<br>
                            ti ricordiamo che il libro che hai in prestito deve essere restituito a breve.
                        </p>
                        
                        <div class="book-info">
                            <p class="book-title">"%s"</p>
                            <p class="book-author">di %s</p>
                        </div>
                        
                        <div class="warning-box">
                            <h3>📅 Data di scadenza: %s</h3>
                            <p style="color: #856404; margin-top: 10px;">
                                Ti preghiamo di restituire il libro entro la data indicata per evitare sanzioni.
                            </p>
                        </div>
                        
                        <div class="footer">
                            <p>Grazie per la tua collaborazione! 📚</p>
                            <p style="color: #ff9800; font-weight: 600; margin-top: 10px;">Il team di SmartLibrary</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(username, bookTitle, bookAuthor, 
                         dueDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    }

    private String buildLoanExpiredEmailHtml(String username, String bookTitle, String bookAuthor, 
                                            java.time.LocalDateTime dueDate) {
        return """
            <!DOCTYPE html>
            <html lang="it">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body {
                        font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                        line-height: 1.6;
                        color: #333;
                        max-width: 600px;
                        margin: 0 auto;
                        padding: 20px;
                        background-color: #f4f4f4;
                    }
                    .container {
                        background: linear-gradient(135deg, #dc3545 0%%, #c82333 100%%);
                        border-radius: 10px;
                        padding: 40px;
                        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                    }
                    .content {
                        background: white;
                        border-radius: 8px;
                        padding: 40px;
                        margin-top: 20px;
                    }
                    .header {
                        text-align: center;
                        color: white;
                        margin-bottom: 30px;
                    }
                    .header h1 {
                        margin: 0;
                        font-size: 32px;
                        font-weight: 700;
                        text-transform: uppercase;
                        letter-spacing: 2px;
                    }
                    .expired-badge {
                        background: #dc3545;
                        color: white;
                        padding: 15px 30px;
                        border-radius: 25px;
                        display: inline-block;
                        font-weight: 700;
                        margin: 20px 0;
                        font-size: 20px;
                        text-transform: uppercase;
                        letter-spacing: 1px;
                        animation: pulse 2s infinite;
                    }
                    @keyframes pulse {
                        0%% { transform: scale(1); }
                        50%% { transform: scale(1.05); }
                        100%% { transform: scale(1); }
                    }
                    .book-info {
                        background: linear-gradient(135deg, #f8d7da 0%%, #f5c6cb 100%%);
                        border-left: 4px solid #dc3545;
                        padding: 25px;
                        border-radius: 8px;
                        margin: 25px 0;
                    }
                    .book-title {
                        font-size: 22px;
                        font-weight: 700;
                        color: #721c24;
                        margin: 0 0 10px 0;
                    }
                    .book-author {
                        font-size: 16px;
                        color: #721c24;
                        margin: 0;
                    }
                    .urgent-box {
                        background: #dc3545;
                        border: 3px solid #c82333;
                        border-radius: 8px;
                        padding: 30px;
                        margin: 25px 0;
                        text-align: center;
                        color: white;
                    }
                    .urgent-box h3 {
                        color: white;
                        margin-top: 0;
                        font-size: 24px;
                        font-weight: 700;
                        text-transform: uppercase;
                    }
                    .urgent-box p {
                        color: white;
                        font-size: 18px;
                        font-weight: 600;
                        margin: 15px 0;
                    }
                    .footer {
                        text-align: center;
                        margin-top: 30px;
                        padding-top: 20px;
                        border-top: 2px solid #eee;
                        color: #777;
                        font-size: 14px;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🚨 IL TEMPO È SCADUTO</h1>
                    </div>
                    <div class="content">
                        <div style="text-align: center;">
                            <div class="expired-badge">⚠️ SCADENZA RAGGIUNTA</div>
                        </div>
                        
                        <div style="background: #e3f2fd; border: 2px solid #2196f3; border-radius: 8px; padding: 15px; margin: 20px 0; text-align: center;">
                            <p style="color: #1976d2; font-size: 16px; font-weight: 600; margin: 0;">
                                ℹ️ <strong>Ignora questa mail se hai già restituito il libro</strong>
                            </p>
                        </div>
                        
                        <p style="font-size: 18px; text-align: center; color: #555;">
                            Ciao <strong style="color: #dc3545;">%s</strong>,<br>
                            <strong style="color: #dc3545; font-size: 20px;">IL TEMPO PER LA RESTITUZIONE È SCADUTO!</strong>
                        </p>
                        
                        <div class="book-info">
                            <p class="book-title">"%s"</p>
                            <p class="book-author">di %s</p>
                        </div>
                        
                        <div class="urgent-box">
                            <h3>⏰ Data di scadenza: %s</h3>
                            <p>⚠️ IL LIBRO DOVEVA ESSERE RESTITUITO ENTRO QUESTA DATA</p>
                            <p style="font-size: 16px; margin-top: 20px;">
                                Ti preghiamo di <strong>RESTITUIRE IMMEDIATAMENTE</strong> il libro presso la biblioteca.
                            </p>
                            <p style="font-size: 16px;">
                                Potrebbero essere applicate sanzioni per il ritardo nella restituzione.
                            </p>
                        </div>
                        
                        <div style="background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; border-radius: 4px; margin: 20px 0;">
                            <p style="color: #856404; margin: 0;">
                                <strong>📞 Contatta la biblioteca</strong> se hai bisogno di assistenza o se hai già restituito il libro.
                            </p>
                        </div>
                        
                        <div class="footer">
                            <p style="color: #dc3545; font-weight: 600;">Restituisci il libro il prima possibile! 📚</p>
                            <p style="color: #dc3545; font-weight: 600; margin-top: 10px;">Il team di SmartLibrary</p>
                        </div>
                    </div>
                </div>
            </body>
            </html>
            """.formatted(username, bookTitle, bookAuthor, 
                         dueDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
    }
}

