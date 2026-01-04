package in.kenz.bookmyshow.booking.service;

import java.util.UUID;

public interface BookingService {

    UUID bookSeat(UUID showId, String seatNumber, UUID userId);

    void payBooking(UUID showId, UUID bookingId, String paymentId);
}