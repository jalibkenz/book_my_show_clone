package in.kenz.bookmyshow.donation.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import in.kenz.bookmyshow.donation.entity.Donation;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class DonationCertificateService {

    public byte[] generateCertificate(Donation donation) {

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Document document = new Document();
            PdfWriter.getInstance(document, out);

            document.open();

            Font title = new Font(Font.FontFamily.HELVETICA, 22, Font.BOLD);
            Font body = new Font(Font.FontFamily.HELVETICA, 14);

            document.add(new Paragraph("Certificate of Appreciation\n\n", title));
            document.add(new Paragraph(
                    "This certificate is proudly presented to\n\n",
                    body
            ));
            document.add(new Paragraph(donation.getName() + "\n\n", title));

            document.add(new Paragraph(
                    "For the generous donation of ₹" + donation.getAmount(),
                    body
            ));

            document.add(new Paragraph("\nReceipt No: " + donation.getId(), body));
            document.add(new Paragraph(
                    "Date: " + donation.getDonatedAt(),
                    body
            ));

            document.close();

            return out.toByteArray();
        }
        catch (Exception e) {
            throw new IllegalStateException("Failed to generate PDF certificate", e);
        }
    }
}