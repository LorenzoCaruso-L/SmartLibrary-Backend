# SmartLibrary - Backend API

Backend Spring Boot per l'applicazione SmartLibrary, una piattaforma stile IMDb per la gestione di libri in biblioteca.

## 🚀 Funzionalità

- ✅ **Catalogo Libri** - Ricerca avanzata con filtri multipli (titolo, autore, genere, anno)
- ✅ **Autenticazione JWT** - Sistema di login/registrazione sicuro
- ✅ **Prenotazioni** - Gli utenti possono prenotare libri disponibili
- ✅ **Email con PDF** - Invio automatico di ticket PDF via email dopo prenotazione
- ✅ **Recensioni** - Sistema di recensioni con rating 1-5 stelle
- ✅ **Profilo Utente** - Gestione prenotazioni personali
- ✅ **Pannello Admin** - Gestione completa libri, prenotazioni e utenti

## 📋 Requisiti

- Java 17+
- Maven 3.6+

## 🔧 Installazione

1. Clona il repository
2. Installa le dipendenze:
```bash
mvn clean install
```

3. Avvia il server:
```bash
mvn spring-boot:run
```

Il server sarà disponibile su `http://localhost:8080`

## 📚 Documentazione API

Vedi [API_DOCUMENTATION.md](API_DOCUMENTATION.md) per la documentazione completa di tutti gli endpoint.

## 🔐 Credenziali di Test

**Utente Admin:**
- Username: `admin`
- Password: `admin123`

**Database H2 Console:**
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:smartlibrary`
- Username: `sa`
- Password: (vuoto)

## 🗂️ Struttura Progetto

```
src/main/java/com/example/smartlibrary/
├── config/          # Configurazioni (Security, JWT)
├── controller/      # REST Controllers
├── dto/            # Data Transfer Objects
├── model/          # Entità JPA
├── repository/     # Repository JPA
├── security/       # Componenti sicurezza (JWT, UserDetails)
└── service/        # Business Logic
    └── impl/       # Implementazioni servizi
```

## 🔑 Autenticazione

Il backend usa **JWT (JSON Web Token)**. Dopo il login, includi il token in tutte le richieste protette:

```
Authorization: Bearer <token>
```

## 📧 Configurazione Email

Per abilitare l'invio email (opzionale in sviluppo), configura in `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**Nota:** Se l'email non è configurata, le prenotazioni funzionano comunque (solo il log mostrerà un warning).

## 🧪 Testing

Esegui i test:
```bash
mvn test
```

## 📝 Note per il Frontend

- Tutti gli endpoint accettano CORS da qualsiasi origine
- Le richieste devono includere `Content-Type: application/json`
- Il token JWT va incluso nell'header `Authorization: Bearer <token>`
- Gli endpoint admin richiedono il ruolo `ROLE_ADMIN`

## 🛠️ Tecnologie Utilizzate

- Spring Boot 4.0.0
- Spring Security + JWT
- Spring Data JPA
- H2 Database (in-memory)
- OpenPDF (generazione PDF)
- Spring Mail (invio email)
- JUnit 5 + Mockito (testing)

## 📄 Licenza

Questo progetto è parte di un progetto universitario.

---

**Sviluppato per SmartLibrary** 📚

