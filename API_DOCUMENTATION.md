# SmartLibrary - Documentazione API Backend

## Base URL
```
http://localhost:8080
```

## Autenticazione

Il backend utilizza **JWT (JSON Web Token)** per l'autenticazione. Dopo il login, includi il token in tutte le richieste protette nell'header:
```
Authorization: Bearer <token>
```

---

## Endpoint Pubblici (senza autenticazione)

### 1. Registrazione Utente
**POST** `/auth/register`

**Body:**
```json
{
  "username": "mario",
  "password": "password123",
  "email": "mario@example.com"
}
```

**Response 200:**
```json
{
  "id": 1,
  "username": "mario",
  "email": "mario@example.com",
  "role": "ROLE_USER"
}
```

---

### 2. Login
**POST** `/auth/login`

**Body:**
```json
{
  "username": "mario",
  "password": "password123"
}
```

**Response 200:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "username": "mario",
  "role": "ROLE_USER"
}
```

**Salva il token** e usalo nelle richieste successive!

---

### 3. Ricerca Libri (Catalogo)
**GET** `/api/books`

**Query Parameters (tutti opzionali, possono essere combinati):**
- `title` - ricerca per titolo (parziale, case-insensitive)
- `author` - ricerca per autore (parziale, case-insensitive)
- `genre` - ricerca per genere (esatto, case-insensitive)
- `year` - ricerca per anno di pubblicazione

**Esempi:**
- `/api/books` - tutti i libri
- `/api/books?title=rosa` - libri con "rosa" nel titolo
- `/api/books?author=Orwell&genre=Distopia` - combinazione filtri
- `/api/books?year=1980` - libri del 1980

**Response 200:**
```json
[
  {
    "id": 1,
    "title": "Il nome della rosa",
    "author": "Umberto Eco",
    "publicationYear": 1980,
    "genre": "Storico",
    "copiesAvailable": 3,
    "description": "Un giallo medievale...",
    "averageRating": 4.5,
    "reviewsCount": 12
  }
]
```

---

### 4. Dettaglio Libro
**GET** `/api/books/{id}`

**Response 200:**
```json
{
  "id": 1,
  "title": "Il nome della rosa",
  "author": "Umberto Eco",
  "publicationYear": 1980,
  "genre": "Storico",
  "copiesAvailable": 3,
  "description": "Un giallo medievale...",
  "averageRating": 4.5,
  "reviewsCount": 12
}
```

**Response 404:** Libro non trovato

---

### 5. Recensioni di un Libro
**GET** `/reviews/book/{bookId}`

**Response 200:**
```json
[
  {
    "id": 1,
    "user": {
      "id": 2,
      "username": "mario"
    },
    "book": {
      "id": 1,
      "title": "Il nome della rosa"
    },
    "rating": 5,
    "comment": "Libro fantastico!"
  }
]
```

---

## Endpoint Protetti (richiedono autenticazione)

### 6. Profilo Utente
**GET** `/profile`

**Headers:** `Authorization: Bearer <token>`

**Response 200:**
```json
{
  "id": 1,
  "username": "mario",
  "email": "mario@example.com",
  "role": "ROLE_USER"
}
```

---

### 7. Prenotazioni Utente
**GET** `/profile/reservations`

**Headers:** `Authorization: Bearer <token>`

**Response 200:**
```json
[
  {
    "id": 1,
    "bookId": 1,
    "bookTitle": "Il nome della rosa",
    "reservationDate": "2024-01-15",
    "active": true,
    "collected": false
  }
]
```

---

### 8. Prenota un Libro
**POST** `/reservations/{bookId}`

**Headers:** `Authorization: Bearer <token>`

**Response 200:**
```json
"Reservation created"
```

**Response 400:** 
- "No copies available" - nessuna copia disponibile
- "You already reserved this book" - prenotazione già esistente

**Nota:** Dopo la prenotazione, l'utente riceverà una email con un PDF allegato contenente il ticket di ritiro.

---

### 9. Cancella Prenotazione
**DELETE** `/reservations/{reservationId}`

**Headers:** `Authorization: Bearer <token>`

**Response 204:** Cancellazione riuscita

**Response 403:** Non sei il proprietario della prenotazione

---

### 10. Aggiungi Recensione
**POST** `/reviews/{bookId}`

**Headers:** `Authorization: Bearer <token>`

**Body:**
```json
{
  "rating": 5,
  "comment": "Libro fantastico, lo consiglio!"
}
```

**Validazione:**
- `rating` deve essere tra 1 e 5
- L'utente deve aver **ritirato** il libro (flag `collected = true` impostato dall'admin)
- Non è possibile recensire lo stesso libro due volte

**Response 200:**
```json
{
  "id": 1,
  "rating": 5,
  "comment": "Libro fantastico, lo consiglio!",
  ...
}
```

**Response 400:**
- "Book must be collected before reviewing" - devi prima ritirare il libro
- "Review already submitted" - recensione già presente

---

## Endpoint Admin (richiedono ruolo ADMIN)

### 11. Crea Libro
**POST** `/admin/books`

**Headers:** `Authorization: Bearer <token>` (admin)

**Body:**
```json
{
  "title": "Nuovo Libro",
  "author": "Autore",
  "publicationYear": 2024,
  "genre": "Fantasy",
  "copiesAvailable": 5,
  "description": "Descrizione del libro..."
}
```

**Response 200:** Libro creato

---

### 12. Aggiorna Libro
**PUT** `/admin/books/{id}`

**Headers:** `Authorization: Bearer <token>` (admin)

**Body:** (stesso formato della creazione)

**Response 200:** Libro aggiornato

---

### 13. Elimina Libro
**DELETE** `/admin/books/{id}`

**Headers:** `Authorization: Bearer <token>` (admin)

**Response 204:** Libro eliminato

---

### 14. Lista Tutte le Prenotazioni
**GET** `/admin/reservations`

**Headers:** `Authorization: Bearer <token>` (admin)

**Response 200:**
```json
[
  {
    "id": 1,
    "bookId": 1,
    "bookTitle": "Il nome della rosa",
    "reservationDate": "2024-01-15",
    "active": true,
    "collected": false
  }
]
```

---

### 15. Marca Libro come Ritirato
**POST** `/admin/reservations/{id}/collected`

**Headers:** `Authorization: Bearer <token>` (admin)

**Response 200:** Prenotazione aggiornata

**Nota:** Quando un libro viene marcato come ritirato (`collected = true`), l'utente può finalmente recensirlo.

---

### 16. Sospendi Utente
**POST** `/admin/users/{id}/suspend`

**Headers:** `Authorization: Bearer <token>` (admin)

**Response 200:** Utente sospeso

---

### 17. Riabilita Utente
**POST** `/admin/users/{id}/enable`

**Headers:** `Authorization: Bearer <token>` (admin)

**Response 200:** Utente riabilitato

---

### 18. Elimina Recensione (Moderazione)
**DELETE** `/reviews/{reviewId}`

**Headers:** `Authorization: Bearer <token>` (admin)

**Response 204:** Recensione eliminata

---

## Codici di Risposta HTTP

- **200 OK** - Richiesta riuscita
- **201 Created** - Risorsa creata
- **204 No Content** - Operazione riuscita senza contenuto
- **400 Bad Request** - Errore di validazione o logica
- **401 Unauthorized** - Token mancante o non valido
- **403 Forbidden** - Permessi insufficienti (es. non sei admin)
- **404 Not Found** - Risorsa non trovata

---

## Note Importanti per il Frontend

1. **CORS:** Il backend accetta richieste da qualsiasi origine (`@CrossOrigin("*")`)

2. **Gestione Token:**
   - Salva il token dopo il login
   - Includilo in tutte le richieste protette: `Authorization: Bearer <token>`
   - Se ricevi 401, reindirizza al login

3. **Flusso Prenotazione:**
   - Utente prenota → riceve email con PDF
   - Va in biblioteca e ritira il libro
   - Admin marca come "collected" → utente può recensire

4. **Ruoli:**
   - `ROLE_USER` - utente normale
   - `ROLE_ADMIN` - amministratore

5. **Utente Admin di Test:**
   - Username: `admin`
   - Password: `admin123`
   - Email: `admin@smartlibrary.it`

---

## Configurazione Email (per sviluppo)

Per testare l'invio email, configura in `application.properties`:
```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

**Nota:** In sviluppo, se l'email non è configurata, la prenotazione funziona comunque (solo il log mostrerà un warning).

---

## Database

Il progetto usa **H2 in-memory database** per sviluppo. I dati vengono persi al riavvio.

Per vedere i dati: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:smartlibrary`
- Username: `sa`
- Password: (vuoto)

---

## Avvio Backend

```bash
mvn spring-boot:run
```

Il server sarà disponibile su `http://localhost:8080`

