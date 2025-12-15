package com.example.smartlibrary.facade;

import com.example.smartlibrary.dto.ReservationDto;
import com.example.smartlibrary.factory.BookFactory;
import com.example.smartlibrary.model.Book;
import com.example.smartlibrary.model.Reservation;
import com.example.smartlibrary.model.Review;
import com.example.smartlibrary.model.User;
import com.example.smartlibrary.repository.BookRepository;
import com.example.smartlibrary.repository.ReservationRepository;
import com.example.smartlibrary.repository.ReviewRepository;
import com.example.smartlibrary.repository.UserRepository;
import com.example.smartlibrary.service.BookService;
import com.example.smartlibrary.service.ReservationService;
import com.example.smartlibrary.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Component
public class AdminFacade {

    private final BookService bookService;
    private final ReservationService reservationService;
    private final UserService userService;
    private final BookFactory bookFactory;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminFacade(BookService bookService,
                       ReservationService reservationService,
                       UserService userService,
                       BookFactory bookFactory,
                       BookRepository bookRepository,
                       UserRepository userRepository,
                       ReviewRepository reviewRepository,
                       ReservationRepository reservationRepository,
                       PasswordEncoder passwordEncoder) {
        this.bookService = bookService;
        this.reservationService = reservationService;
        this.userService = userService;
        this.bookFactory = bookFactory;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
        this.reservationRepository = reservationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Book createBook(Book request) {
        Book sanitized = bookFactory.copyOf(request);
        return bookService.save(sanitized);
    }

    public Book updateBook(Long id, Book request) {
        Book sanitized = bookFactory.copyOf(request);
        return bookService.update(id, sanitized);
    }

    public void deleteBook(Long id) {
        bookService.delete(id);
    }

    public List<ReservationDto> listReservations() {
        return reservationService.allReservations();
    }

    public void markCollected(Long id) {
        reservationService.markCollected(id);
    }

    public void suspendUser(Long id) {
        userService.suspendUser(id);
    }

    public void enableUser(Long id) {
        userService.enableUser(id);
    }

    @Transactional
    public Map<String, Object> generateFakeReviews(int reviewsPerBook) {
        List<Book> allBooks = bookRepository.findAll();
        if (allBooks.isEmpty()) {
            return Map.of(
                "message", "Nessun libro trovato nel database",
                "reviewsCreated", 0
            );
        }

        String[] fakeUsernames = {
            "MarioRossi", "LuigiBianchi", "GiuliaVerdi", "MarcoNeri", "SofiaRosa",
            "AlessandroBlu", "FrancescaGialli", "LucaArancio", "ChiaraViola", "AndreaCeleste",
            "ValentinaGrigio", "DavideMarrone", "ElenaAzzurro", "SimoneTurchese", "MartinaIndaco",
            "FedericoBeige", "GiorgiaCiano", "RiccardoMagenta", "ElisaCoral", "TommasoSalmon"
        };

        Random random = new Random();
        int totalReviewsCreated = 0;
        int usersCreated = 0;
        int reservationsCreated = 0;

        List<User> fakeUsers = new ArrayList<>();
        for (String username : fakeUsernames) {
            User user = userRepository.findByUsername(username).orElse(null);
            if (user == null) {
                user = new User();
                user.setUsername(username);
                user.setEmail(username.toLowerCase() + "@fake.com");
                user.setPassword(passwordEncoder.encode("password123"));
                user.setRole("ROLE_USER");
                user.setEnabled(true);
                user.setLocked(false);
                user = userRepository.save(user);
                usersCreated++;
            }
            fakeUsers.add(user);
        }

        String[] positiveComments = {
            "Libro fantastico! L'ho letto tutto d'un fiato.",
            "Molto coinvolgente, consigliatissimo!",
            "Scrittura eccellente e trama avvincente.",
            "Uno dei migliori libri che abbia mai letto.",
            "Storia emozionante, non riuscivo a smettere di leggere.",
            "Personaggi ben caratterizzati e trama interessante.",
            "Bellissimo, lo consiglio a tutti!",
            "Scrittura fluida e coinvolgente.",
            "Un capolavoro, assolutamente da leggere!",
            "Mi è piaciuto molto, lo rileggerei volentieri."
        };

        String[] neutralComments = {
            "Libro discreto, niente di eccezionale.",
            "Carino ma non mi ha entusiasmato particolarmente.",
            "Buona lettura, anche se un po' lento in alcuni punti.",
            "Interessante ma con qualche pecca.",
            "Nella media, poteva essere meglio.",
            "Leggibile ma non memorabile.",
            "Ok, ma non è il mio genere preferito.",
            "Buono ma non eccezionale."
        };

        String[] negativeComments = {
            "Non mi è piaciuto molto, troppo lento.",
            "Deludente, mi aspettavo di più.",
            "Trama poco coinvolgente.",
            "Non è riuscito a catturarmi.",
            "Troppo prevedibile per i miei gusti."
        };

        for (Book book : allBooks) {
            int reviewsToCreate = Math.min(reviewsPerBook, fakeUsernames.length);
            Collections.shuffle(fakeUsers);
            
            for (int i = 0; i < reviewsToCreate; i++) {
                User user = fakeUsers.get(i);
                
                if (reviewRepository.existsByBookIdAndUserId(book.getId(), user.getId())) {
                    continue;
                }

                boolean hasReservation = reservationRepository.existsByUserIdAndBookId(user.getId(), book.getId());
                if (!hasReservation) {
                    Reservation reservation = new Reservation();
                    reservation.setUser(user);
                    reservation.setBook(book);
                    reservation.setReservationDate(LocalDate.now().minusDays(random.nextInt(30) + 1));
                    reservation.setActive(true);
                    reservation.setCollected(true);
                    reservation.setCollectedDate(java.time.LocalDateTime.now().minusDays(random.nextInt(20) + 1));
                    reservation.setDueDate(java.time.LocalDateTime.now().plusDays(random.nextInt(10)));
                    reservation.setPickupCode(UUID.randomUUID().toString().substring(0, 8));
                    reservationRepository.save(reservation);
                    reservationsCreated++;
                }

                int rating;
                int rand = random.nextInt(100);
                if (rand < 60) {
                    rating = random.nextBoolean() ? 5 : 4;
                } else if (rand < 90) {
                    rating = 3;
                } else {
                    rating = random.nextBoolean() ? 1 : 2;
                }

                String comment;
                if (rating >= 4) {
                    comment = positiveComments[random.nextInt(positiveComments.length)];
                } else if (rating == 3) {
                    comment = neutralComments[random.nextInt(neutralComments.length)];
                } else {
                    comment = negativeComments[random.nextInt(negativeComments.length)];
                }

                Review review = new Review();
                review.setUser(user);
                review.setBook(book);
                review.setRating(rating);
                review.setComment(comment);
                reviewRepository.save(review);
                totalReviewsCreated++;
            }
        }

        return Map.of(
            "message", "Recensioni fake generate con successo",
            "booksProcessed", allBooks.size(),
            "reviewsPerBook", reviewsPerBook,
            "totalReviewsCreated", totalReviewsCreated,
            "usersCreated", usersCreated,
            "reservationsCreated", reservationsCreated
        );
    }
}

