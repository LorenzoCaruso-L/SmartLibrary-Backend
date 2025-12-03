# 📋 Riepilogo Backend SmartLibrary

## ✅ Completato

### 1. Autenticazione e Autorizzazione
- ✅ Sistema JWT completo
- ✅ Login/Registrazione
- ✅ Protezione endpoint con ruoli (USER/ADMIN)
- ✅ Gestione utenti con flag enabled/locked

### 2. Catalogo Libri
- ✅ Ricerca avanzata con filtri multipli (titolo, autore, genere, anno)
- ✅ Dettaglio libro con descrizione
- ✅ Calcolo automatico media rating e conteggio recensioni
- ✅ Endpoint pubblici per catalogo

### 3. Prenotazioni
- ✅ Prenotazione libri (solo se disponibili)
- ✅ Controllo doppie prenotazioni
- ✅ Gestione copie disponibili
- ✅ Generazione codice pickup univoco
- ✅ Invio email con PDF ticket (configurabile)
- ✅ Lista prenotazioni utente
- ✅ Cancellazione prenotazioni
- ✅ Marcatura ritiro (admin)

### 4. Recensioni
- ✅ Sistema recensioni con rating 1-5 stelle
- ✅ Validazione: solo libri ritirati possono essere recensiti
- ✅ Prevenzione recensioni duplicate
- ✅ Lista recensioni per libro
- ✅ Eliminazione recensioni (admin)

### 5. Profilo Utente
- ✅ Visualizzazione profilo
- ✅ Lista prenotazioni personali
- ✅ Gestione stato account

### 6. Pannello Admin
- ✅ CRUD completo libri (crea, aggiorna, elimina)
- ✅ Visualizzazione tutte le prenotazioni
- ✅ Marcatura libri come ritirati
- ✅ Sospensione/Riabilitazione utenti
- ✅ Eliminazione recensioni (moderazione)

### 7. Servizi di Supporto
- ✅ Generazione PDF ticket prenotazione
- ✅ Invio email con allegato PDF
- ✅ Calcolo statistiche recensioni

### 8. Testing
- ✅ Test unitari BookService
- ✅ Test unitari ReservationService
- ✅ Mockito per isolamento test

### 9. Documentazione
- ✅ README.md completo
- ✅ API_DOCUMENTATION.md dettagliata
- ✅ Dati di esempio (import.sql)
- ✅ Utente admin di test

## 📁 Struttura File Principali

### Controllers
- `AuthController` - Login/Registrazione
- `BookController` - Catalogo libri (pubblico)
- `ReservationController` - Prenotazioni utente
- `ReviewController` - Recensioni
- `ProfileController` - Profilo utente
- `AdminController` - Gestione admin

### Services
- `BookService` - Logica business libri
- `ReservationService` - Logica prenotazioni
- `ReviewService` - Logica recensioni
- `UserService` - Gestione utenti
- `NotificationService` - Invio email
- `PdfService` - Generazione PDF

### Security
- `SecurityConfig` - Configurazione Spring Security
- `JwtService` - Gestione token JWT
- `JwtAuthenticationFilter` - Filtro autenticazione
- `CustomUserDetailsService` - Caricamento utenti

### Models
- `Book` - Entità libro (con descrizione)
- `User` - Entità utente (con ruoli)
- `Reservation` - Entità prenotazione
- `Review` - Entità recensione

## 🔑 Endpoint Principali

### Pubblici
- `POST /auth/register` - Registrazione
- `POST /auth/login` - Login
- `GET /api/books` - Ricerca libri
- `GET /api/books/{id}` - Dettaglio libro
- `GET /reviews/book/{id}` - Recensioni libro

### Protetti (USER)
- `GET /profile` - Profilo utente
- `GET /profile/reservations` - Prenotazioni utente
- `POST /reservations/{bookId}` - Prenota libro
- `DELETE /reservations/{id}` - Cancella prenotazione
- `POST /reviews/{bookId}` - Aggiungi recensione

### Protetti (ADMIN)
- `POST /admin/books` - Crea libro
- `PUT /admin/books/{id}` - Aggiorna libro
- `DELETE /admin/books/{id}` - Elimina libro
- `GET /admin/reservations` - Tutte le prenotazioni
- `POST /admin/reservations/{id}/collected` - Marca ritirato
- `POST /admin/users/{id}/suspend` - Sospendi utente
- `POST /admin/users/{id}/enable` - Riabilita utente
- `DELETE /reviews/{id}` - Elimina recensione

## 🧱 Pattern di Progettazione Implementati

- **Factory Method – `BookFactory`**  
  Centralizza la creazione e la copia dei `Book` e dei `BookResponse`, assicurando che tutti i controller e i service istanzino le entità con regole uniformi (sanitizzazione campi, default, formati). Questo riduce la duplicazione e rende più semplice evolvere la struttura dei libri.

- **Builder – `ReservationTicketBuilder`**  
  Incapsula la costruzione del ticket di prenotazione usato dal `PdfService`. Il builder consente di aggiungere facilmente nuovi elementi (istruzioni, QR, note) senza toccare il codice di generazione PDF e rende leggibile la configurazione dei dati del ticket.

- **Facade – `AdminFacade`**  
  Espone un’unica API interna per le operazioni amministrative (gestione libri, prenotazioni, utenti) e viene utilizzata dal `AdminController`. In questo modo il controller rimane snello e tutta la logica cross-service è centralizzata e riusabile.

## 🚀 Come Avviare

1. **Installa dipendenze:**
   ```bash
   mvn clean install
   ```

2. **Avvia il server:**
   ```bash
   mvn spring-boot:run
   ```

3. **Server disponibile su:**
   ```
   http://localhost:8080
   ```

4. **Database H2 Console:**
   ```
   http://localhost:8080/h2-console
   JDBC URL: jdbc:h2:mem:smartlibrary
   Username: sa
   Password: (vuoto)
   ```

## 🔐 Credenziali Test

**Admin:**
- Username: `admin`
- Password: `admin123`

## 📧 Configurazione Email (Opzionale)

Per abilitare l'invio email, configura in `application.properties`:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**Nota:** Se non configurata, le prenotazioni funzionano comunque (solo warning nel log).

## ✨ Pronto per il Frontend!

Il backend è completo e pronto per l'integrazione. Tutti gli endpoint sono documentati in `API_DOCUMENTATION.md`.

**Prossimi passi per il frontend:**
1. Integrare autenticazione JWT
2. Implementare chiamate API per catalogo
3. Gestire prenotazioni e recensioni
4. Creare pannello admin

---

**Backend completato al 100%! 🎉**

