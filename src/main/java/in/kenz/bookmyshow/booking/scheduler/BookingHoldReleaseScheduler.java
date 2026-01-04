package in.kenz.bookmyshow.booking.scheduler;

import in.kenz.bookmyshow.booking.entity.Booking;
import in.kenz.bookmyshow.booking.enums.BookingShowSeatStatus;
import in.kenz.bookmyshow.booking.repository.BookingRepository;
import in.kenz.bookmyshow.seat.entity.Seat;
import in.kenz.bookmyshow.seat.repository.SeatRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BookingHoldReleaseScheduler {

    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    @Value("${booking.hold.ttlMinutes:5}")
    private long holdTtlMinutes;

    // run every minute
    @Scheduled(fixedDelayString = "PT1M")
    @Transactional
    public void releaseExpiredHolds() {
        OffsetDateTime cutoff = OffsetDateTime.now().minusMinutes(holdTtlMinutes);

        List<Booking> expired = bookingRepository.findByStatusAndCreatedAtBefore(BookingShowSeatStatus.HELD, cutoff);

        for (Booking b : expired) {
            try {
                // find associated seat efficiently
                Seat seat = seatRepository.findByShowIdAndSeatNumber(b.getShowId(), b.getSeatNumber())
                        .orElse(null);

                if (seat != null) {
                    // clear booking association and mark available
                    seat.setBookingShowSeatStatus(BookingShowSeatStatus.AVAILABLE);
                    seat.setBookingId(null);
                    seatRepository.save(seat);
                }

                b.setStatus(BookingShowSeatStatus.CANCELLED);
                bookingRepository.save(b);

            } catch (Exception ex) {
                log.error("Failed to release expired booking {}", b.getId(), ex);
            }
        }
    }
}
