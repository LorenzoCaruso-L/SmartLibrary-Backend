package com.example.smartlibrary.service;

import com.example.smartlibrary.model.Reservation;
import com.example.smartlibrary.repository.ReservationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoanSchedulerService {

    private static final Logger log = LoggerFactory.getLogger(LoanSchedulerService.class);

    private final ReservationRepository reservationRepository;
    private final NotificationService notificationService;

    public LoanSchedulerService(ReservationRepository reservationRepository,
                                NotificationService notificationService) {
        this.reservationRepository = reservationRepository;
        this.notificationService = notificationService;
    }

    @Scheduled(fixedRate = 10000)
    @Transactional
    public void checkLoanDeadlines() {
        LocalDateTime now = LocalDateTime.now();
        List<Reservation> activeLoans = reservationRepository.findByCollectedTrueAndDueDateIsNotNull();
        
        if (activeLoans.isEmpty()) {
            return;
        }
        
        for (Reservation loan : activeLoans) {
            if (loan.getDueDate() == null) {
                continue;
            }
            
            if (loan.getUser() == null) {
                log.error("ERRORE: Prestito ID {} ha User NULL!", loan.getId());
                continue;
            }
            if (loan.getBook() == null) {
                log.error("ERRORE: Prestito ID {} ha Book NULL!", loan.getId());
                continue;
            }
            
            String userEmail = loan.getUser().getEmail();
            if (userEmail != null && userEmail.endsWith("@fake.com")) {
                continue;
            }
            
            LocalDateTime dueDate = loan.getDueDate();
            java.time.Duration duration = java.time.Duration.between(now, dueDate);
            long totalSeconds = duration.getSeconds();
            long daysUntilDue = duration.toDays();
            
            boolean isShortTermLoan = daysUntilDue == 0 && totalSeconds < 86400;
            
            if (isShortTermLoan) {
                if (totalSeconds <= 30 && totalSeconds > 0 && !loan.isReminderSent()) {
                    try {
                        notificationService.sendLoanReminder(loan);
                        loan.setReminderSent(true);
                        reservationRepository.save(loan);
                        log.info("✅ Email promemoria inviata - Prestito ID: {} - Utente: {}", 
                                loan.getId(), loan.getUser().getUsername());
                    } catch (Exception e) {
                        log.error("Errore nell'invio email promemoria per prestito ID: {}", loan.getId(), e);
                    }
                }
                
                if (totalSeconds <= 0 && !loan.isExpiredSent()) {
                    try {
                        notificationService.sendLoanExpired(loan);
                        loan.setExpiredSent(true);
                        reservationRepository.save(loan);
                        log.info("✅ Email scadenza inviata - Prestito ID: {} - Utente: {}", 
                                loan.getId(), loan.getUser().getUsername());
                    } catch (Exception e) {
                        log.error("Errore nell'invio email scadenza per prestito ID: {}", loan.getId(), e);
                    }
                }
            } else {
                if (daysUntilDue <= 3 && daysUntilDue >= 2 && !loan.isReminderSent()) {
                    try {
                        notificationService.sendLoanReminder(loan);
                        loan.setReminderSent(true);
                        reservationRepository.save(loan);
                        log.info("✅ Email promemoria inviata - Prestito ID: {} - Utente: {} - Scade tra {} giorni", 
                                loan.getId(), loan.getUser().getUsername(), daysUntilDue);
                    } catch (Exception e) {
                        log.error("Errore nell'invio email promemoria per prestito ID: {}", loan.getId(), e);
                    }
                }
                
                if ((now.isAfter(dueDate) || now.isEqual(dueDate) || daysUntilDue == 0) && !loan.isExpiredSent()) {
                    try {
                        notificationService.sendLoanExpired(loan);
                        loan.setExpiredSent(true);
                        reservationRepository.save(loan);
                        log.info("✅ Email scadenza inviata - Prestito ID: {} - Utente: {}", 
                                loan.getId(), loan.getUser().getUsername());
                    } catch (Exception e) {
                        log.error("Errore nell'invio email scadenza per prestito ID: {}", loan.getId(), e);
                    }
                }
            }
        }
    }
}

