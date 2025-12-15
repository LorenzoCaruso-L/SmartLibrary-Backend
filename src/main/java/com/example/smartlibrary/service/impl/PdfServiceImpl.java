package com.example.smartlibrary.service.impl;

import com.example.smartlibrary.builder.ReservationTicket;
import com.example.smartlibrary.builder.ReservationTicketBuilder;
import com.example.smartlibrary.model.Reservation;
import com.example.smartlibrary.service.PdfService;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

@Service
public class PdfServiceImpl implements PdfService {

    @Override
    public byte[] generateReservationTicket(Reservation reservation) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ReservationTicket ticket = ReservationTicketBuilder.create()
                    .withPickupCode(reservation.getPickupCode())
                    .withUsername(reservation.getUser().getUsername())
                    .withBookTitle(reservation.getBook().getTitle())
                    .withReservationDate(reservation.getReservationDate())
                    .build();

            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, baos);
            document.open();

            // Colori personalizzati
            Color primaryColor = new Color(102, 126, 234); // #667eea
            Color lightGray = new Color(248, 249, 250);
            Color darkGray = new Color(51, 51, 51);
            Color successGreen = new Color(40, 167, 69);

            // Header con gradiente (simulato con rettangolo colorato)
            PdfPTable headerTable = new PdfPTable(1);
            headerTable.setWidthPercentage(100);
            headerTable.setSpacingBefore(0);
            headerTable.setSpacingAfter(20);
            
            PdfPCell headerCell = new PdfPCell();
            headerCell.setBackgroundColor(primaryColor);
            headerCell.setBorder(Rectangle.NO_BORDER);
            headerCell.setPadding(30);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 28, Color.WHITE);
            Paragraph headerText = new Paragraph("📚 SmartLibrary", headerFont);
            headerText.setAlignment(Element.ALIGN_CENTER);
            headerCell.addElement(headerText);
            
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Color.WHITE);
            Paragraph subtitle = new Paragraph("Ticket Prenotazione", subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            subtitle.setSpacingBefore(5);
            headerCell.addElement(subtitle);
            
            headerTable.addCell(headerCell);
            document.add(headerTable);

            // Badge di successo
            PdfPTable badgeTable = new PdfPTable(1);
            badgeTable.setWidthPercentage(100);
            badgeTable.setSpacingAfter(25);
            
            PdfPCell badgeCell = new PdfPCell();
            badgeCell.setBackgroundColor(successGreen);
            badgeCell.setBorder(Rectangle.NO_BORDER);
            badgeCell.setPadding(12);
            badgeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            Font badgeFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, Color.WHITE);
            Paragraph badgeText = new Paragraph("✓ Prenotazione Confermata", badgeFont);
            badgeText.setAlignment(Element.ALIGN_CENTER);
            badgeCell.addElement(badgeText);
            
            badgeTable.addCell(badgeCell);
            document.add(badgeTable);

            // Codice di ritiro evidenziato
            PdfPTable codeTable = new PdfPTable(1);
            codeTable.setWidthPercentage(100);
            codeTable.setSpacingAfter(20);
            
            PdfPCell codeCell = new PdfPCell();
            codeCell.setBackgroundColor(new Color(255, 243, 205)); // Giallo chiaro
            codeCell.setBorderColor(new Color(255, 193, 7)); // Giallo
            codeCell.setBorderWidth(2);
            codeCell.setBorder(Rectangle.BOX);
            codeCell.setPadding(20);
            codeCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            
            Font codeLabelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, new Color(133, 100, 4));
            Paragraph codeLabel = new Paragraph("CODICE RITIRO", codeLabelFont);
            codeLabel.setAlignment(Element.ALIGN_CENTER);
            codeLabel.setSpacingAfter(10);
            codeCell.addElement(codeLabel);
            
            Font codeFont = FontFactory.getFont(FontFactory.COURIER_BOLD, 32, new Color(133, 100, 4));
            Paragraph codeText = new Paragraph(ticket.getPickupCode(), codeFont);
            codeText.setAlignment(Element.ALIGN_CENTER);
            codeText.setSpacingAfter(5);
            codeCell.addElement(codeText);
            
            Font codeInstructionFont = FontFactory.getFont(FontFactory.HELVETICA, 10, new Color(133, 100, 4));
            Paragraph codeInstruction = new Paragraph("Presenta questo codice al banco prestiti", codeInstructionFont);
            codeInstruction.setAlignment(Element.ALIGN_CENTER);
            codeCell.addElement(codeInstruction);
            
            codeTable.addCell(codeCell);
            document.add(codeTable);

            // Informazioni libro in box colorato
            PdfPTable bookTable = new PdfPTable(1);
            bookTable.setWidthPercentage(100);
            bookTable.setSpacingAfter(20);
            
            PdfPCell bookCell = new PdfPCell();
            bookCell.setBackgroundColor(lightGray);
            bookCell.setBorderColor(primaryColor);
            bookCell.setBorderWidth(3);
            bookCell.setBorder(Rectangle.LEFT);
            bookCell.setPadding(20);
            bookCell.setPaddingLeft(25);
            
            Font bookTitleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, primaryColor);
            Paragraph bookTitle = new Paragraph("📖 " + ticket.getBookTitle(), bookTitleFont);
            bookTitle.setSpacingAfter(8);
            bookCell.addElement(bookTitle);
            
            Font bookAuthorFont = FontFactory.getFont(FontFactory.HELVETICA, 14, darkGray);
            Paragraph bookAuthor = new Paragraph("di " + reservation.getBook().getAuthor(), bookAuthorFont);
            bookAuthor.setSpacingAfter(15);
            bookCell.addElement(bookAuthor);
            
            bookTable.addCell(bookCell);
            document.add(bookTable);

            // Dettagli prenotazione in tabella
            PdfPTable detailsTable = new PdfPTable(2);
            detailsTable.setWidthPercentage(100);
            detailsTable.setSpacingAfter(20);
            detailsTable.setWidths(new float[]{40, 60});
            
            Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, darkGray);
            Font valueFont = FontFactory.getFont(FontFactory.HELVETICA, 11, darkGray);
            
            addDetailRow(detailsTable, "👤 Utente:", ticket.getUsername(), labelFont, valueFont);
            addDetailRow(detailsTable, "📅 Data prenotazione:", ticket.getReservationDate().toString(), labelFont, valueFont);
            
            document.add(detailsTable);

            // Istruzioni in box informativo
            PdfPTable infoTable = new PdfPTable(1);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingAfter(10);
            
            PdfPCell infoCell = new PdfPCell();
            infoCell.setBackgroundColor(new Color(209, 236, 241)); // Azzurro chiaro
            infoCell.setBorderColor(new Color(12, 84, 96)); // Azzurro scuro
            infoCell.setBorderWidth(2);
            infoCell.setBorder(Rectangle.LEFT);
            infoCell.setPadding(15);
            infoCell.setPaddingLeft(20);
            
            Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 11, new Color(12, 84, 96));
            Paragraph infoText = new Paragraph("⏰ " + ticket.getInstructions(), infoFont);
            infoCell.addElement(infoText);
            
            infoTable.addCell(infoCell);
            document.add(infoTable);

            // Footer
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 10, new Color(119, 119, 119));
            Paragraph footer = new Paragraph("\nIl team di SmartLibrary", footerFont);
            footer.setAlignment(Element.ALIGN_CENTER);
            footer.setSpacingBefore(30);
            document.add(footer);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new IllegalStateException("Errore nella generazione del PDF", e);
        }
    }

    private void addDetailRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setBackgroundColor(new Color(248, 249, 250));
        labelCell.setBorder(Rectangle.BOX);
        labelCell.setBorderWidth(1);
        labelCell.setPadding(10);
        labelCell.setBorderColor(new Color(222, 226, 230));
        
        PdfPCell valueCell = new PdfPCell(new Phrase(value, valueFont));
        valueCell.setBorder(Rectangle.BOX);
        valueCell.setBorderWidth(1);
        valueCell.setPadding(10);
        valueCell.setBorderColor(new Color(222, 226, 230));
        
        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}

