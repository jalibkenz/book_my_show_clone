package in.kenz.bookmyshow.screenandseat.dto;

import in.kenz.bookmyshow.booking.enums.BookingShowSeatStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SeatDto {
    private UUID id;
    private UUID showId;
    private String seatNumber;
    private BookingShowSeatStatus bookingShowSeatStatus;
}

