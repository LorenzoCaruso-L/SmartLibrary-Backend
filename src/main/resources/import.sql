-- Libri di esempio
INSERT INTO book (title, author, publication_year, genre, copies_available, description)
VALUES ('Il nome della rosa', 'Umberto Eco', 1980, 'Storico', 3, 'Un giallo medievale ambientato in un monastero benedettino nel 1327. Un frate francescano investiga su una serie di omicidi misteriosi.');

INSERT INTO book (title, author, publication_year, genre, copies_available, description)
VALUES ('1984', 'George Orwell', 1949, 'Distopia', 4, 'Un romanzo distopico che descrive un futuro totalitario dove il Grande Fratello controlla ogni aspetto della vita dei cittadini.');

-- Utente admin di esempio (password: admin123)
-- La password è già hashata con BCrypt
INSERT INTO users (username, email, password, role, enabled, account_non_locked)
VALUES ('admin', 'admin@smartlibrary.it', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ROLE_ADMIN', true, true);
