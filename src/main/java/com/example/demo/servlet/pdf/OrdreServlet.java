package com.example.demo.servlet.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@WebServlet("/GenerateOrdreMission")
public class OrdreServlet extends HttpServlet {
    private BaseFont baseFont;

    @Override
    public void init() throws ServletException {
        try {
            // Mets une vraie police arabe ici (ex: NotoNaskhArabic-Regular.ttf ou Amiri-Regular.ttf)
            String fontPath = getServletContext().getRealPath("/WEB-INF/fonts/NotoNaskhArabic-Regular.ttf");
            baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (Exception e) {
            throw new ServletException("Erreur police: " + e.getMessage(), e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            Font titleFont = new Font(baseFont, 16, Font.BOLD);
            Font subTitleFont = new Font(baseFont, 14, Font.BOLD);
            Font normalFont = new Font(baseFont, 12, Font.NORMAL);
            Font boldFont = new Font(baseFont, 12, Font.BOLD);

            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=OrdreDeMission.pdf");

            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            document.open();

            // === Titre principal ===
            Paragraph title = new Paragraph("ORDRE DE MISSION - أمر مأمورية", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            // === Numéro et date ===
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Paragraph reference = new Paragraph("N°045/BAMIS/2025 du " + today.format(formatter), subTitleFont);
            reference.setAlignment(Element.ALIGN_CENTER);
            reference.setSpacingBefore(10);
            reference.setSpacingAfter(20);
            document.add(reference);

            // === Tableau infos personnelles ===
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(10);

            addTableRow(infoTable, "Nom et Prénom / الإسم واللقب", request.getParameter("nom"), boldFont, normalFont);
            addTableRow(infoTable, "Nationalité / الجنسية", request.getParameter("nationalite"), boldFont, normalFont);
            addTableRow(infoTable, "Fonction / الوظيفة", request.getParameter("fonction"), boldFont, normalFont);
            addTableRow(infoTable, "Objet de la mission / سبب المهمة", request.getParameter("objet"), boldFont, normalFont);

            document.add(infoTable);

            // === Tableau Voyage ===
            PdfPTable voyageTable = new PdfPTable(4);
            voyageTable.setWidthPercentage(100);
            voyageTable.setSpacingBefore(20);

            voyageTable.addCell(createHeaderCell("Compagnie\nشركة الطيران", boldFont));
            voyageTable.addCell(createHeaderCell("Itinéraire\nالطريق", boldFont));
            voyageTable.addCell(createHeaderCell("Départ\nالمغادرة", boldFont));
            voyageTable.addCell(createHeaderCell("Arrivée\nالعودة", boldFont));

            voyageTable.addCell(createCell(request.getParameter("compagnie"), normalFont));
            voyageTable.addCell(createCell(request.getParameter("itineraire"), normalFont));
            voyageTable.addCell(createCell(request.getParameter("depart"), normalFont));
            voyageTable.addCell(createCell(request.getParameter("arrivee"), normalFont));

            document.add(voyageTable);

            // === Frais de Mission ===
            Paragraph fraisTitle = new Paragraph("\nFrais de Mission", boldFont);
            fraisTitle.setSpacingBefore(20);
            fraisTitle.setSpacingAfter(10);
            document.add(fraisTitle);

            PdfPTable fraisTable = new PdfPTable(3);
            fraisTable.setWidthPercentage(100);
            fraisTable.addCell(createHeaderCell("Durée (jours)", boldFont));
            fraisTable.addCell(createHeaderCell("Montant par jour (MRU)", boldFont));
            fraisTable.addCell(createHeaderCell("Montant total (MRU)", boldFont));

            String duree = request.getParameter("duree");
            String montantJour = request.getParameter("montantJour");
            int total = 0;
            try {
                total = Integer.parseInt(duree) * Integer.parseInt(montantJour);
            } catch (Exception ignored) {}

            fraisTable.addCell(createCell(duree, normalFont));
            fraisTable.addCell(createCell(montantJour, normalFont));
            fraisTable.addCell(createCell(String.valueOf(total), normalFont));

            document.add(fraisTable);

            // === Pied de page ===
            Paragraph footer = new Paragraph(
                    "Fait à " + request.getParameter("faitA") + ", le " + today.format(formatter),
                    normalFont
            );
            footer.setAlignment(Element.ALIGN_LEFT);
            footer.setSpacingBefore(30);
            document.add(footer);

            Paragraph signature = new Paragraph("DIRECTION GENERALE - الإدارة العامة", boldFont);
            signature.setAlignment(Element.ALIGN_RIGHT);
            document.add(signature);

            document.close();

        } catch (Exception e) {
            throw new ServletException("Erreur PDF: " + e.getMessage(), e);
        }
    }

    // === Helpers ===
    private PdfPCell createHeaderCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        return cell;
    }

    private PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        return cell;
    }

    private void addTableRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Phrase(label, labelFont));
        labelCell.setPadding(5);
        table.addCell(labelCell);

        PdfPCell valueCell = new PdfPCell(new Phrase(value != null ? value : "", valueFont));
        valueCell.setPadding(5);
        valueCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        table.addCell(valueCell);
    }
}
