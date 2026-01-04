package in.kenz.bookmyshow.booking.service;

import in.kenz.bookmyshow.booking.entity.Booking;
import in.kenz.bookmyshow.screenandseat.entity.Show;

public interface TicketService {
    byte[] generateTicketPdf(Booking booking, Show show, String userName);
}

