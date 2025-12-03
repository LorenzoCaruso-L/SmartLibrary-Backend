# 🧪 Come Testare l'API SmartLibrary

## ⚠️ IMPORTANTE: Questo è un'API REST, non una pagina web!

Il backend **NON ha una pagina di login HTML**. È un'API che risponde a richieste HTTP con JSON.

## 🔧 Metodi per Testare

### 1. **Postman** (Consigliato)

Scarica Postman: https://www.postman.com/downloads/

#### Test Login:
1. Crea una nuova richiesta `POST`
2. URL: `http://localhost:8080/auth/login`
3. Headers: `Content-Type: application/json`
4. Body (raw JSON):
```json
{
  "username": "admin",
  "password": "admin123"
}
```
5. Invia → Riceverai un token JWT

#### Test con Token:
1. Crea una nuova richiesta `GET`
2. URL: `http://localhost:8080/profile`
3. Headers:
   - `Authorization: Bearer <token_ricevuto>`
   - `Content-Type: application/json`
4. Invia → Vedrai il profilo utente

---

### 2. **cURL** (Terminale)

#### Login:
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"admin\",\"password\":\"admin123\"}"
```

#### Con Token:
```bash
curl -X GET http://localhost:8080/profile \
  -H "Authorization: Bearer <token>" \
  -H "Content-Type: application/json"
```

---

### 3. **Browser** (Solo endpoint GET pubblici)

Puoi testare nel browser solo gli endpoint pubblici:

- ✅ `http://localhost:8080/api/books` - Catalogo libri
- ✅ `http://localhost:8080/api/books/1` - Dettaglio libro
- ✅ `http://localhost:8080/reviews/book/1` - Recensioni
- ✅ `http://localhost:8080/` - Pagina info API

**NON puoi fare login dal browser!** Devi usare Postman o cURL.

---

### 4. **JavaScript (Frontend)**

```javascript
// Login
const response = await fetch('http://localhost:8080/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    username: 'admin',
    password: 'admin123'
  })
});

const data = await response.json();
const token = data.token; // Salva questo token!

// Usa il token per richieste protette
const profile = await fetch('http://localhost:8080/profile', {
  headers: {
    'Authorization': `Bearer ${token}`,
    'Content-Type': 'application/json'
  }
});
```

---

## 📋 Esempi Completi

### 1. Registrazione Nuovo Utente
```bash
POST http://localhost:8080/auth/register
Content-Type: application/json

{
  "username": "mario",
  "password": "password123",
  "email": "mario@example.com"
}
```

### 2. Ricerca Libri
```bash
GET http://localhost:8080/api/books?title=rosa
```

### 3. Prenota Libro (dopo login)
```bash
POST http://localhost:8080/reservations/1
Authorization: Bearer <token>
```

### 4. Aggiungi Recensione (dopo login e ritiro libro)
```bash
POST http://localhost:8080/reviews/1
Authorization: Bearer <token>
Content-Type: application/json

{
  "rating": 5,
  "comment": "Libro fantastico!"
}
```

---

## ✅ Checklist Test

- [ ] Server avviato su `http://localhost:8080`
- [ ] Test catalogo: `GET /api/books` funziona
- [ ] Test registrazione: `POST /auth/register` crea utente
- [ ] Test login: `POST /auth/login` restituisce token
- [ ] Test profilo: `GET /profile` con token funziona
- [ ] Test prenotazione: `POST /reservations/{id}` con token funziona

---

## 🆘 Problemi Comuni

### "403 Forbidden"
- ✅ Verifica di aver incluso il token JWT nell'header `Authorization`
- ✅ Verifica che il token non sia scaduto
- ✅ Per endpoint admin, verifica di essere loggato come admin

### "401 Unauthorized"
- ✅ Token mancante o non valido
- ✅ Rifai il login per ottenere un nuovo token

### "404 Not Found"
- ✅ Verifica che l'URL sia corretto
- ✅ Verifica che il server sia avviato

---

**Il frontend dovrà implementare le pagine HTML. Il backend fornisce solo API REST! 🚀**

