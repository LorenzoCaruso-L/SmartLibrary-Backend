# 🚀 Come Avviare il Progetto SmartLibrary

## ⚠️ IMPORTANTE: Non usare `javac` direttamente!

Spring Boot richiede **Maven** per gestire tutte le dipendenze. Non puoi compilare i file Java singolarmente.

## 📋 Prerequisiti

1. **Java 17+** installato
2. **Maven** installato e configurato nel PATH

### Verifica installazione:

```powershell
java -version
mvn -version
```

Se Maven non è installato, scaricalo da: https://maven.apache.org/download.cgi

## ✅ Metodo Corretto per Avviare

### 1. Apri il terminale nella cartella del progetto:
```powershell
cd C:\Users\lown1\OneDrive\Desktop\SmartLibrary-
```

### 2. Installa le dipendenze (prima volta):
```powershell
mvn clean install
```

### 3. Avvia il server:
```powershell
mvn spring-boot:run
```

Oppure, se preferisci compilare prima e poi eseguire:
```powershell
mvn clean package
java -jar target/SmartLibrary--0.0.1-SNAPSHOT.jar
```

## 🎯 Cosa Succede

1. Maven scarica tutte le dipendenze (Spring Boot, JWT, H2, ecc.)
2. Compila tutto il progetto
3. Avvia il server Spring Boot su `http://localhost:8080`

## ✅ Verifica che Funzioni

Dopo l'avvio, vedrai nel terminale:
```
Started SmartLibraryApplication in X.XXX seconds
```

Poi puoi testare:
- Browser: `http://localhost:8080/api/books`
- Postman: `GET http://localhost:8080/api/books`

## 🔧 Se Maven non è nel PATH

Se `mvn` non viene riconosciuto:

1. **Scarica Maven** da https://maven.apache.org/download.cgi
2. **Estrai** in una cartella (es. `C:\Program Files\Apache\maven`)
3. **Aggiungi al PATH**:
   - Apri "Variabili d'ambiente" in Windows
   - Aggiungi `C:\Program Files\Apache\maven\bin` al PATH
   - Riavvia il terminale

## 🆘 Alternativa: Usa Maven Wrapper

Se Maven non è installato, puoi usare il wrapper incluso:

**Windows:**
```powershell
.\mvnw.cmd clean install
.\mvnw.cmd spring-boot:run
```

**Linux/Mac:**
```bash
./mvnw clean install
./mvnw spring-boot:run
```

## 📝 Note

- **NON** compilare singoli file Java con `javac`
- **USA** sempre Maven per gestire il progetto Spring Boot
- Il primo avvio può richiedere tempo (scarica dipendenze)

---

**Il progetto è configurato correttamente, usa Maven per avviarlo! 🎉**

