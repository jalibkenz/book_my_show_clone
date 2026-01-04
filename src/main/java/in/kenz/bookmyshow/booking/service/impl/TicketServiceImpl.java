package in.kenz.bookmyshow.booking.service.impl;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import in.kenz.bookmyshow.booking.entity.Booking;
import in.kenz.bookmyshow.booking.service.TicketService;
import in.kenz.bookmyshow.screenandseat.entity.Show;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class TicketServiceImpl implements TicketService {

    @Override
    public byte[] generateTicketPdf(Booking booking, Show show, String userName) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, baos);
            document.open();

            document.add(new Paragraph("Ticket for " + show.getScreenName()));
            document.add(new Paragraph("Movie ID: " + show.getMovieId()));
            document.add(new Paragraph("Show: " + show.getStartTime() + " - " + show.getEndTime()));
            document.add(new Paragraph("Seat: " + booking.getSeatNumber()));
            document.add(new Paragraph("Name: " + userName));
            document.add(new Paragraph("Booking ID: " + booking.getId()));

            document.close();
            return baos.toByteArray();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to generate ticket PDF", ex);
        }
    }
}

