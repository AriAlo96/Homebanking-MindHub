package com.mindhub.homebanking.models;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TransactionPDF {
    private Account account;
    private List<Transaction> listTransactions;
    private static final String LOGO_PATH = "C:\\Users\\harme\\OneDrive\\Escritorio\\MindHub\\JAVA\\homebanking\\src\\main\\resources\\static\\web\\assets\\images\\Logo.png";

    public TransactionPDF(List<Transaction> listTransactions, Account account) {
        this.listTransactions = listTransactions;
        this.account = account;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(4);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);

        cell.setPhrase(new Phrase("Date", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Description", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("TiTransaction type", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Amount", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for(Transaction transaction : listTransactions) {
            table.addCell(transaction.getDate().format(formatter));
            table.addCell(transaction.getDescription());
            table.addCell(String.valueOf(transaction.getType()));
            table.addCell((String.format(String.valueOf(transaction.getAmount()), "$0,0.00")));
        }
    }

    public void usePDFExport(HttpServletResponse response) throws DocumentException, IOException {
        Document doc = new Document(PageSize.A4);
        PdfWriter.getInstance(doc, response.getOutputStream());
        doc.open();


        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
        Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
        Font detailsFont = FontFactory.getFont(FontFactory.HELVETICA, 12);


        Image logo = Image.getInstance(LOGO_PATH);
        logo.scaleToFit(200, 200);
        logo.setAlignment(Image.ALIGN_LEFT);

        doc.add(logo);


        Paragraph title = new Paragraph("Transactions list", titleFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        title.setSpacingAfter(10);
        doc.add(title);


        Paragraph accountInfo = new Paragraph("Account information", headerFont);
        accountInfo.setAlignment(Paragraph.ALIGN_LEFT);
        doc.add(accountInfo);

        doc.add(new Paragraph("Account number: " + account.getNumber(), detailsFont));
        doc.add(new Paragraph("Balance: " + account.getBalance(), detailsFont));

        LocalDate creationDate = account.getCreationDate();
        String formateDate = creationDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        doc.add(new Paragraph("Fecha de creación: " + formateDate, detailsFont));

        // Tabla de transacciones
        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);

        // Encabezado de la tabla
        writeTableHeader(table);

        // Contenido de la tabla
        writeTableData(table);

        doc.add(table);
        doc.close();
    }
}
