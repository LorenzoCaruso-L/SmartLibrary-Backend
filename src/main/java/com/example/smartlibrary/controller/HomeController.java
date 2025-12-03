package com.example.smartlibrary.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return """
                <html>
                <head>
                    <title>SmartLibrary API</title>
                    <style>
                        body { font-family: Arial, sans-serif; padding: 40px; background: #f4f4f4; }
                        .container { max-width: 800px; margin: 0 auto; background: white; padding: 30px; border-radius: 8px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                        h1 { color: #2c3e50; }
                        .endpoint { background: #ecf0f1; padding: 10px; margin: 10px 0; border-radius: 4px; font-family: monospace; }
                        .method { color: #27ae60; font-weight: bold; }
                        .public { color: #3498db; }
                        .protected { color: #e74c3c; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <h1>📚 SmartLibrary - Backend API</h1>
                        <p>Benvenuto! Questo è il backend API REST per SmartLibrary.</p>
                        
                        <h2>🔓 Endpoint Pubblici (senza autenticazione)</h2>
                        <div class="endpoint">
                            <span class="method">POST</span> <span class="public">/auth/register</span> - Registrazione utente
                        </div>
                        <div class="endpoint">
                            <span class="method">POST</span> <span class="public">/auth/login</span> - Login (restituisce JWT token)
                        </div>
                        <div class="endpoint">
                            <span class="method">GET</span> <span class="public">/api/books</span> - Catalogo libri
                        </div>
                        <div class="endpoint">
                            <span class="method">GET</span> <span class="public">/api/books/{id}</span> - Dettaglio libro
                        </div>
                        <div class="endpoint">
                            <span class="method">GET</span> <span class="public">/reviews/book/{id}</span> - Recensioni libro
                        </div>
                        
                        <h2>🔒 Endpoint Protetti (richiedono JWT token)</h2>
                        <p>Includi nell'header: <code>Authorization: Bearer &lt;token&gt;</code></p>
                        <div class="endpoint">
                            <span class="method">GET</span> <span class="protected">/profile</span> - Profilo utente
                        </div>
                        <div class="endpoint">
                            <span class="method">POST</span> <span class="protected">/reservations/{bookId}</span> - Prenota libro
                        </div>
                        <div class="endpoint">
                            <span class="method">POST</span> <span class="protected">/reviews/{bookId}</span> - Aggiungi recensione
                        </div>
                        
                        <h2>👑 Endpoint Admin (richiedono ruolo ADMIN)</h2>
                        <div class="endpoint">
                            <span class="method">POST</span> <span class="protected">/admin/books</span> - Crea libro
                        </div>
                        <div class="endpoint">
                            <span class="method">GET</span> <span class="protected">/admin/reservations</span> - Tutte le prenotazioni
                        </div>
                        
                        <h2>🧪 Come Testare</h2>
                        <p><strong>1. Login:</strong></p>
                        <pre>POST http://localhost:8080/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "admin123"
}</pre>
                        
                        <p><strong>2. Usa il token ricevuto:</strong></p>
                        <pre>GET http://localhost:8080/profile
Authorization: Bearer &lt;token&gt;</pre>
                        
                        <p><strong>📚 Documentazione completa:</strong> Vedi <code>API_DOCUMENTATION.md</code></p>
                        <p><strong>🔧 Test rapido:</strong> <a href="/api/books">/api/books</a> (catalogo libri)</p>
                    </div>
                </body>
                </html>
                """;
    }
}
