package in.kenz.bookmyshow.donation.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import in.kenz.bookmyshow.donation.entity.Donation;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class DonationCertificatePdfService {

    public byte[] generate(Donation donation) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // ================= PAGE SETUP =================
            Rectangle pageSize = PageSize.A4.rotate(); // A4 Landscape
            Document document = new Document(pageSize, 40, 40, 40, 40);
            PdfWriter writer = PdfWriter.getInstance(document, out);

            document.open();
            PdfContentByte canvas = writer.getDirectContent();

            // ================= BORDER =================
            Rectangle border = new Rectangle(
                    document.left() - 10,
                    document.bottom() - 10,
                    document.right() + 10,
                    document.top() + 10
            );
            border.setBorder(Rectangle.BOX);
            border.setBorderWidth(3);
            border.setBorderColor(new BaseColor(34, 139, 34)); // Green
            canvas.rectangle(border);

            // ================= FONTS =================
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 30, Font.BOLD,
                    new BaseColor(34, 139, 34));
            Font nameFont = new Font(Font.FontFamily.HELVETICA, 24, Font.BOLD);
            Font bodyFont = new Font(Font.FontFamily.HELVETICA, 15);
            Font labelFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD);

            // ================= CONTENT TABLE =================
            PdfPTable table = new PdfPTable(1);
            table.setWidthPercentage(100);
            table.setSpacingBefore(70);

            table.addCell(cell("CERTIFICATE OF APPRECIATION", titleFont));
            table.addCell(spacer(20));

            // ✅ SAFE DONOR NAME
            String donorName =
                    donation.getName() != null && !donation.getName().isBlank()
                            ? donation.getName().toUpperCase()
                            : "VALUED SUPPORTER";

            table.addCell(cell(donorName, nameFont));
            table.addCell(spacer(20));

            table.addCell(cell(
                    "In recognition of your generous contribution of ₹"
                            + donation.getAmount()
                            + " towards our mission.",
                    bodyFont
            ));

            table.addCell(spacer(35));

            // ================= DETAILS TABLE =================
            PdfPTable details = new PdfPTable(2);
            details.setWidthPercentage(60);
            details.setHorizontalAlignment(Element.ALIGN_CENTER);
            details.setWidths(new float[]{1, 2});

            DateTimeFormatter df = DateTimeFormatter.ofPattern("dd MMM yyyy");

            details.addCell(detailLabel("Receipt Number", labelFont));
            details.addCell(detailValue(donation.getId().toString()));

            // ✅ SAFE DONATION DATE
            String donatedDate =
                    donation.getDonatedAt() != null
                            ? donation.getDonatedAt().format(df)
                            : "—";

            details.addCell(detailLabel("Donation Date", labelFont));
            details.addCell(detailValue(donatedDate));

            table.addCell(details);
            table.addCell(spacer(45));

            table.addCell(cell("Issued by", bodyFont));
            table.addCell(cell("JALIB CODES", labelFont));

            document.add(table);
            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to generate donation certificate PDF", e
            );
        }
    }

    // ================= HELPER METHODS =================

    private PdfPCell cell(String text, Font font) {
        PdfPCell c = new PdfPCell(new Phrase(text, font));
        c.setBorder(Rectangle.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_CENTER);
        c.setPadding(6);
        return c;
    }

    private PdfPCell spacer(float height) {
        PdfPCell c = new PdfPCell(new Phrase(""));
        c.setFixedHeight(height);
        c.setBorder(Rectangle.NO_BORDER);
        return c;
    }

    private PdfPCell detailLabel(String text, Font font) {
        PdfPCell c = new PdfPCell(new Phrase(text, font));
        c.setBorder(Rectangle.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_RIGHT);
        c.setPaddingRight(10);
        return c;
    }

    private PdfPCell detailValue(String text) {
        PdfPCell c = new PdfPCell(new Phrase(text));
        c.setBorder(Rectangle.NO_BORDER);
        c.setHorizontalAlignment(Element.ALIGN_LEFT);
        return c;
    }
}