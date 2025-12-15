package com.example.smartlibrary.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

@Component
@Order(1)
public class DatabaseMigrationListener {

    private static final Logger log = LoggerFactory.getLogger(DatabaseMigrationListener.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void migrateDatabase() {
        try {
            log.info("Verifica e aggiornamento schema database...");
            

            boolean tableExists = tableExists("reservation");
            if (!tableExists) {
                log.info("Tabella reservation non trovata, verrà creata da Hibernate");
                return;
            }


            addColumnIfNotExists("reservation", "collected_date", "DATETIME");
            addColumnIfNotExists("reservation", "due_date", "DATETIME");
            addColumnIfNotExists("reservation", "reminder_sent", "INTEGER DEFAULT 0");
            addColumnIfNotExists("reservation", "expired_sent", "INTEGER DEFAULT 0");

            log.info("Schema database verificato e aggiornato con successo");
        } catch (Exception e) {
            log.error("Errore durante la migrazione del database", e);
        }
    }

    private boolean tableExists(String tableName) {
        try (var connection = jdbcTemplate.getDataSource().getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, tableName, null)) {
                return tables.next();
            }
        } catch (Exception e) {
            log.error("Errore nel controllo esistenza tabella {}", tableName, e);
            return false;
        }
    }

    private boolean columnExists(String tableName, String columnName) {
        try (var connection = jdbcTemplate.getDataSource().getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();
            try (ResultSet columns = metaData.getColumns(null, null, tableName, columnName)) {
                return columns.next();
            }
        } catch (Exception e) {
            log.error("Errore nel controllo esistenza colonna {}.{}", tableName, columnName, e);
            return false;
        }
    }

    private void addColumnIfNotExists(String tableName, String columnName, String columnDefinition) {
        try {
            if (!columnExists(tableName, columnName)) {
                String sql = String.format("ALTER TABLE %s ADD COLUMN %s %s", tableName, columnName, columnDefinition);
                log.info("Aggiunta colonna {}.{} con definizione: {}", tableName, columnName, columnDefinition);
                jdbcTemplate.execute(sql);
                log.info("Colonna {}.{} aggiunta con successo", tableName, columnName);
            } else {
                log.debug("Colonna {}.{} già esistente", tableName, columnName);
            }
        } catch (Exception e) {
            log.error("Errore nell'aggiunta colonna {}.{}", tableName, columnName, e);
        }
    }
}

