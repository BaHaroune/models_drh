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
            String fontPath = getServletContext().getRealPath("/WEB-INF/fonts/NotoSansArabic.ttf");
            baseFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        } catch (Exception e) {
            throw new ServletException("Erreur police: " + e.getMessage(), e);
        }
    }

    private void addTableRow3Columns(PdfPTable table, String labelFr, String value, String labelAr, Font labelFont, Font valueFont) {
        float paddingVertical = 8f; // espace vertical supplémentaire

        // Colonne française (gauche)
        PdfPCell cellFr = new PdfPCell(new Phrase(labelFr, labelFont));
        cellFr.setBorder(Rectangle.NO_BORDER);
        cellFr.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellFr.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        cellFr.setPaddingTop(paddingVertical);
        cellFr.setPaddingBottom(paddingVertical);
        table.addCell(cellFr);

        // Colonne valeur (centre)
        PdfPCell cellValue = new PdfPCell(new Phrase(value != null ? value : "", valueFont));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellValue.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        cellValue.setPaddingTop(paddingVertical);
        cellValue.setPaddingBottom(paddingVertical);
        table.addCell(cellValue);

        // Colonne arabe (droite)
        PdfPCell cellAr = new PdfPCell();
        cellAr.setBorder(Rectangle.NO_BORDER);
        cellAr.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellAr.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        cellAr.setPaddingTop(paddingVertical);
        cellAr.setPaddingBottom(paddingVertical);

        Phrase phraseAr = new Phrase();
        Chunk chunkAr = new Chunk(labelAr, labelFont);
        phraseAr.add(chunkAr);
        cellAr.setPhrase(phraseAr);

        table.addCell(cellAr);
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            Font titleFont = new Font(baseFont, 14, Font.BOLD);
            Font subTitleFont = new Font(baseFont, 11, Font.NORMAL);
            Font normalFont = new Font(baseFont, 12, Font.NORMAL);
            Font boldFont = new Font(baseFont, 12, Font.BOLD);

            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=OrdreDeMission.pdf");

            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            document.open();

            // === Titre principal ===
            // Créer un tableau à 1 colonne pour centrer correctement le texte
            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setWidthPercentage(100);

// Construire une phrase FR - AR
            Phrase phraseTitle = new Phrase();
            phraseTitle.add(new Chunk("أمر مأمورية", titleFont));
            phraseTitle.add(new Chunk(" - ", titleFont));
            phraseTitle.add(new Chunk("ORDRE DE MISSION", titleFont));

// Une seule cellule centrée
            PdfPCell titleCell = new PdfPCell(phraseTitle);
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            titleTable.addCell(titleCell);

            document.add(titleTable);


            // === Numéro et date ===
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            Paragraph reference = new Paragraph("N°045/BAMIS/2025 du " + today.format(formatter), subTitleFont);
            reference.setAlignment(Element.ALIGN_CENTER);
            reference.setSpacingBefore(5);
            reference.setSpacingAfter(30);
            document.add(reference);

            // === Tableau infos personnelles ===
            PdfPTable infoTable = new PdfPTable(3);
            infoTable.setWidthPercentage(100);
            infoTable.setSpacingBefore(10);

            float[] columnWidths = {30f, 40f, 30f};
            infoTable.setWidths(columnWidths);

            addTableRow3Columns(infoTable, "Nom et Prénom :", request.getParameter("nom"), "ال إسم واللقب :", boldFont, normalFont);
            addTableRow3Columns(infoTable, "Nationalité :", request.getParameter("nationalite"), "الجنسية :", boldFont, normalFont);
            addTableRow3Columns(infoTable, "Fonction :", request.getParameter("fonction"), "الوظيفة :", boldFont, normalFont);
            addTableRow3Columns(infoTable, "Objet de la mission :", request.getParameter("objet"), "سبب المهمة :", boldFont, normalFont);

            document.add(infoTable);
            // === Tableau Voyage ===
            PdfPTable voyageTable = new PdfPTable(4);
            voyageTable.setWidthPercentage(100);
            voyageTable.setSpacingBefore(40);

            voyageTable.addCell(createHeaderCellMixed("Compagnie", "شركة الطيران", boldFont));
            voyageTable.addCell(createHeaderCellMixed("Itinéraire", "الطريق", boldFont));
            voyageTable.addCell(createHeaderCellMixed("Départ", "المغادرة", boldFont));
            voyageTable.addCell(createHeaderCellMixed("Arrivée", "العودة", boldFont));

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
            fraisTable.addCell(createHeaderCell("Durée (jours)", boldFont, false));
            fraisTable.addCell(createHeaderCell("Montant par jour (MRU)", boldFont, false));
            fraisTable.addCell(createHeaderCell("Montant total (MRU)", boldFont, false));


            String duree = request.getParameter("duree");
            String montantJour = request.getParameter("montantJour");
            int total = 0;
            try {
                total = Integer.parseInt(duree) * Integer.parseInt(montantJour);
            } catch (Exception ignored) {
            }

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

            // === Pied de page Signature ===
            PdfPTable sigTable = new PdfPTable(1);
            sigTable.setWidthPercentage(100);

// Construire phrase FR - AR
            Phrase phraseSig = new Phrase();
            phraseSig.add(new Chunk("DIRECTION GENERALE", boldFont));
            phraseSig.add(new Chunk(" - ", boldFont));
            phraseSig.add(new Chunk("الإدارة العامة", boldFont));

// Une seule cellule alignée à droite
            PdfPCell sigCell = new PdfPCell(phraseSig);
            sigCell.setBorder(Rectangle.NO_BORDER);
            sigCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            sigCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
//            sigCell.setPaddingTop(f); // espace vers le haut pour "pousser" vers le bas

            sigTable.addCell(sigCell);

// Ajouter un espace avant la signature pour forcer le bas de page
            sigTable.setSpacingBefore(50f);

            document.add(sigTable);

            document.close();

        } catch (Exception e) {
            throw new ServletException("Erreur PDF: " + e.getMessage(), e);
        }
    }

    // === Helpers ===
    private PdfPCell createHeaderCell(String text, Font font, boolean isArabic) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setRunDirection(isArabic ? PdfWriter.RUN_DIRECTION_RTL : PdfWriter.RUN_DIRECTION_LTR);
        return cell;
    }


    private PdfPCell createHeaderCellMixed(String textFr, String textAr, Font font) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(BaseColor.WHITE);
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        // Phrase qui combine FR (LTR) et AR (RTL)
        Phrase phrase = new Phrase();
        // Partie FR
        Chunk frChunk = new Chunk(textFr + "\n", font);
        phrase.add(frChunk);
        // Partie AR
        Chunk arChunk = new Chunk(textAr, font);
        phrase.add(arChunk);

        cell.setPhrase(phrase);
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
