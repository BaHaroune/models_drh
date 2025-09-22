package com.example.demo.servlet;

import com.example.demo.entity.OrdreMission;
import com.example.demo.util.JPAUtil;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.prefs.Preferences;

@WebServlet("/GenerateOrdreMission")
public class OrdreServlet extends HttpServlet {
    private BaseFont baseFont;

    private EntityManager em;


    @Override
    public void init() throws ServletException {
        em = JPAUtil.getEntityManager();
        try {
            // Mets une vraie police arabe ici (ex: NotoNaskhArabic-Regular.ttf ou Amiri-Regular.ttf)
            String fontPath = getServletContext().getRealPath("/WEB-INF/fonts/NotoSansArabic.ttf");
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
            Font titleFont = new Font(baseFont, 14, Font.BOLD);
            Font subTitleFont = new Font(baseFont, 11, Font.NORMAL);
            Font normalFont = new Font(baseFont, 12, Font.NORMAL);
            Font boldFont = new Font(baseFont, 12, Font.BOLD);

            Document document = new Document(PageSize.A4, 50, 50, 50, 50);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=Ordre de mission.pdf");

            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            document.open();

            // === Titre principal ===
            PdfPTable titleTable = new PdfPTable(1);
            titleTable.setWidthPercentage(100);

            Phrase phraseTitle = new Phrase();
            phraseTitle.add(new Chunk("أمر مأمورية", titleFont));
            phraseTitle.add(new Chunk(" - ", titleFont));
            phraseTitle.add(new Chunk("ORDRE DE MISSION", titleFont));

            PdfPCell titleCell = new PdfPCell(phraseTitle);
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);

            titleTable.addCell(titleCell);
            document.add(titleTable);

            // === Numéro et date ===
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            // ⚠ Ici tu définis la valeur de départ si jamais le compteur n’existe pas encore
            String numero = getNextReferenceNumber(45);

            Paragraph reference = new Paragraph("N°" + numero + "/BAMIS/" + today.getYear()
                    + " du " + today.format(formatter), subTitleFont);

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

            voyageTable.addCell(createHeaderCellMixed("Mode de transport", "وسيلة النقل", boldFont));
            voyageTable.addCell(createHeaderCellMixed("Itinéraire", "الطريق", boldFont));
            voyageTable.addCell(createHeaderCellMixed("Départ", "المغادرة", boldFont));
            voyageTable.addCell(createHeaderCellMixed("Arrivée", "العودة", boldFont));

            // Parsing dates
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dateDepart = "";
            String dateArrivee = "";
            try {
                dateDepart = LocalDate.parse(request.getParameter("depart"), inputFormatter).format(outputFormatter);
                dateArrivee = LocalDate.parse(request.getParameter("arrivee"), inputFormatter).format(outputFormatter);
            } catch (Exception e) {
                dateDepart = request.getParameter("depart");
                dateArrivee = request.getParameter("arrivee");
            }

            voyageTable.addCell(createCell(request.getParameter("compagnie"), normalFont));
            voyageTable.addCell(createCell(request.getParameter("itineraire"), normalFont));
            voyageTable.addCell(createCell(dateDepart, normalFont));
            voyageTable.addCell(createCell(dateArrivee, normalFont));

            document.add(voyageTable);

            // === Frais de Mission ===
            Paragraph fraisTitle = new Paragraph("\nFrais de Mission", boldFont);
            fraisTitle.setSpacingBefore(20);
            fraisTitle.setSpacingAfter(10);
            document.add(fraisTitle);

            PdfPTable fraisTable = new PdfPTable(3);
            fraisTable.setWidthPercentage(100);

            // Choix de la devise
            String typeMission = request.getParameter("typeMission");
            String devise = "interieur".equals(typeMission) ? "MRU" : "€";

            // En-têtes
            fraisTable.addCell(createHeaderCell("Durée (jours)", boldFont, false));
            fraisTable.addCell(createHeaderCell("Montant par jour (" + devise + ")", boldFont, false));
            fraisTable.addCell(createHeaderCell("Montant total (" + devise + ")", boldFont, false));

            // Lignes de données
            String duree = request.getParameter("duree");
            String montantJour = request.getParameter("montantJour");
            String montantTotal = request.getParameter("montantTotal");

            fraisTable.addCell(createCell(duree, normalFont));
            fraisTable.addCell(createCell(montantJour + " " + devise, normalFont));
            fraisTable.addCell(createCell(montantTotal + " " + devise, normalFont));

            document.add(fraisTable);


            // === Création et remplissage de l'entité ===
            OrdreMission ordre = new OrdreMission();
            ordre.setNumero(numero); // ex: "045"
            ordre.setAnnee(today.getYear());
            ordre.setReference("N°" + numero + "/BAMIS/" + today.getYear());
            ordre.setDateCreation(today);

            ordre.setAgentNom(request.getParameter("nom"));
            ordre.setFonction(request.getParameter("fonction"));
            ordre.setNationalite(request.getParameter("nationalite"));
            ordre.setObjet(request.getParameter("objet"));

            ordre.setDestination(request.getParameter("itineraire")); // itinéraire ≈ destination
            try {
                ordre.setDateDebut(LocalDate.parse(request.getParameter("depart"), inputFormatter));
                ordre.setDateFin(LocalDate.parse(request.getParameter("arrivee"), inputFormatter));
            } catch (Exception e) {
                ordre.setDateDebut(null);
                ordre.setDateFin(null);
            }

            ordre.setTransport(request.getParameter("compagnie"));
            ordre.setObservations(request.getParameter("faitA")); // ou un champ dédié

// Champs financiers
            try {
                ordre.setDuree(Integer.parseInt(request.getParameter("duree")));
            } catch (Exception e) {
                ordre.setDuree(0);
            }
            ordre.setMontantJour(request.getParameter("montantJour"));
            ordre.setMontantTotal(request.getParameter("montantTotal"));
            ordre.setTypeMission(request.getParameter("typeMission")); // interieur / etranger

// Sauvegarde en base
            saveOrdreMission(ordre);


            // === Pied de page ===
            Paragraph footer = new Paragraph(
                    "Fait à " + request.getParameter("faitA") + ", le " + today.format(formatter),
                    normalFont
            );
            footer.setAlignment(Element.ALIGN_LEFT);
            footer.setSpacingBefore(30);
            document.add(footer);

            // === Signature ===
            PdfPTable sigTable = new PdfPTable(1);
            sigTable.setWidthPercentage(100);

            Phrase phraseSig = new Phrase();
            phraseSig.add(new Chunk("DIRECTION GENERALE", boldFont));
            phraseSig.add(new Chunk(" - ", boldFont));
            phraseSig.add(new Chunk("الإدارة العامة", boldFont));

            PdfPCell sigCell = new PdfPCell(phraseSig);
            sigCell.setBorder(Rectangle.NO_BORDER);
            sigCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            sigCell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
            sigTable.addCell(sigCell);

            sigTable.setSpacingBefore(50f);
            document.add(sigTable);

            document.close();

        } catch (Exception e) {
            throw new ServletException("Erreur PDF: " + e.getMessage(), e);
        }
    }


    private void saveOrdreMission(OrdreMission ordre) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(ordre);
            tx.commit();
            System.out.println("✅ Ordre de mission sauvegardé !");
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            e.printStackTrace();
        }
    }

    // Fonction pour obtenir le numéro auto-incrémenté
    private void addTableRow3Columns(PdfPTable table, String labelFr, String value, String labelAr, Font labelFont, Font valueFont) {
        float paddingVertical = 8f;

        PdfPCell cellFr = new PdfPCell(new Phrase(labelFr, labelFont));
        cellFr.setBorder(Rectangle.NO_BORDER);
        cellFr.setHorizontalAlignment(Element.ALIGN_LEFT);
        cellFr.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        cellFr.setPaddingTop(paddingVertical);
        cellFr.setPaddingBottom(paddingVertical);
        table.addCell(cellFr);

        PdfPCell cellValue = new PdfPCell(new Phrase(value != null ? value : "", valueFont));
        cellValue.setBorder(Rectangle.NO_BORDER);
        cellValue.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellValue.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        cellValue.setPaddingTop(paddingVertical);
        cellValue.setPaddingBottom(paddingVertical);
        table.addCell(cellValue);

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

    private String getNextReferenceNumber(int startValue) {
        Preferences prefs = Preferences.userRoot().node(this.getClass().getName());
        // prefs.putInt("lastCounter", 43);
        int currentYear = LocalDate.now().getYear();
        int lastYear = prefs.getInt("lastYear", currentYear);
        int counter;

        if (lastYear != currentYear) {
            // Nouvelle année → reset compteur à 0
            counter = 0;
            prefs.putInt("lastYear", currentYear);
        } else {
            // Lire la dernière valeur (ou la valeur de départ si jamais non initialisé)
            counter = prefs.getInt("lastCounter", startValue) + 1;
        }

        // Sauvegarder le nouveau compteur
        prefs.putInt("lastCounter", counter);

        // Retourner sous format 3 chiffres (ex: 001, 045, 123)
        return String.format("%03d", counter);
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
        Phrase phrase = new Phrase();
        phrase.add(new Chunk(textFr + "\n", font));
        phrase.add(new Chunk(textAr, font));
        cell.setPhrase(phrase);
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_RTL);
        return cell;
    }

    private PdfPCell createCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text != null ? text : "", font));
        cell.setPadding(5);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        return cell;
    }
}
