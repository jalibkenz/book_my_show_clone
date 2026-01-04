package in.kenz.bookmyshow.booking;

import in.kenz.bookmyshow.booking.entity.Booking;
import in.kenz.bookmyshow.booking.enums.BookingShowSeatStatus;
import in.kenz.bookmyshow.booking.repository.BookingRepository;
import in.kenz.bookmyshow.booking.scheduler.BookingHoldReleaseScheduler;
import in.kenz.bookmyshow.seat.entity.Seat;
import in.kenz.bookmyshow.seat.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.OffsetDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class BookingHoldReleaseSchedulerTest {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    SeatRepository seatRepository;

    @Test
    void releasesExpiredHeldBookings() {
        UUID showId = UUID.randomUUID();

        // create a booking held 10 minutes ago
        Booking booking = Booking.builder()
                .showId(showId)
                .seatNumber("A1")
                .userId(UUID.randomUUID())
                .createdAt(OffsetDateTime.now().minusMinutes(10))
                .amount(java.math.BigDecimal.TEN)
                .status(BookingShowSeatStatus.HELD)
                .build();

        Booking saved = bookingRepository.save(booking);

        Seat seat = Seat.builder()
                .showId(showId)
                .seatNumber("A1")
                .bookingShowSeatStatus(BookingShowSeatStatus.HELD)
                .bookingId(saved.getId())
                .build();

        seatRepository.save(seat);

        BookingHoldReleaseScheduler scheduler = new BookingHoldReleaseScheduler(bookingRepository, seatRepository);
        scheduler.releaseExpiredHolds();

        Booking after = bookingRepository.findById(saved.getId()).orElseThrow();
        assertThat(after.getStatus()).isEqualTo(BookingShowSeatStatus.CANCELLED);

        Seat afterSeat = seatRepository.findByShowIdAndSeatNumber(showId, "A1").orElseThrow();
        assertThat(afterSeat.getBookingShowSeatStatus()).isEqualTo(BookingShowSeatStatus.AVAILABLE);
        assertThat(afterSeat.getBookingId()).isNull();
    }
}

