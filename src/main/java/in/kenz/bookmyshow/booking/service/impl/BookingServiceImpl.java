package in.kenz.bookmyshow.booking.service.impl;

import in.kenz.bookmyshow.seat.entity.Seat;
import in.kenz.bookmyshow.booking.entity.Booking;
import in.kenz.bookmyshow.booking.enums.BookingShowSeatStatus;
import in.kenz.bookmyshow.seat.repository.SeatRepository;
import in.kenz.bookmyshow.booking.repository.BookingRepository;
import in.kenz.bookmyshow.booking.service.BookingService;
import in.kenz.bookmyshow.show.entity.Show;
import in.kenz.bookmyshow.show.repository.ShowRepository;
import in.kenz.bookmyshow.booking.service.TicketService;
import in.kenz.bookmyshow.user.repository.UserRepository;
import in.kenz.bookmyshow.notification.email.service.EmailService;
import in.kenz.bookmyshow.user.entity.User;
import jakarta.persistence.OptimisticLockException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;
    private final BookingRepository bookingRepository;
    private final TicketService ticketService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    private static final int MAX_RETRIES = 3;

    @Transactional
    @Override
    public UUID bookSeat(
            UUID showId,
            String seatNumber,
            UUID userId
    ) {

        Seat seat =
                seatRepository.lockSeatForBooking(showId, seatNumber)
                        .orElseThrow(() ->
                                new IllegalStateException("Seat does not exist")
                        );

        if (seat.getBookingShowSeatStatus()
                != BookingShowSeatStatus.AVAILABLE) {

            throw new IllegalStateException(
                    "Seat already booked or held"
            );
        }

        Show show = showRepository.findById(showId)
                .orElseThrow(() -> new IllegalStateException("Show does not exist"));

        // create booking record in HELD state and set amount from show
        Booking booking = Booking.builder()
                .showId(showId)
                .seatNumber(seatNumber)
                .userId(userId)
                .createdAt(OffsetDateTime.now())
                .amount(show.getBasePrice() != null ? show.getBasePrice() : java.math.BigDecimal.ZERO)
                .status(BookingShowSeatStatus.HELD)
                .build();

        Booking saved = bookingRepository.save(booking);

        // associate seat -> booking and mark HELD
        seat.setBookingShowSeatStatus(BookingShowSeatStatus.HELD);
        seat.setBookingId(saved.getId());
        seatRepository.save(seat);

        return saved.getId();
    }

    @Transactional
    @Override
    public void payBooking(UUID showId, UUID bookingId, String paymentId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalStateException("Booking does not exist"));

        if (!booking.getShowId().equals(showId)) {
            throw new IllegalArgumentException("Booking does not belong to the provided show");
        }

        if (booking.getStatus() != BookingShowSeatStatus.HELD) {
            throw new IllegalStateException("Booking is not in a payable state");
        }

        // lock the seat by showId & seatNumber to transition it to BOOKED
        Seat seat = seatRepository.lockSeatForBooking(booking.getShowId(), booking.getSeatNumber())
                .orElseThrow(() -> new IllegalStateException("Seat does not exist"));

        if (seat.getBookingId() == null || !seat.getBookingId().equals(bookingId)) {
            throw new IllegalStateException("Booking does not own the seat");
        }

        if (seat.getBookingShowSeatStatus() != BookingShowSeatStatus.HELD) {
            throw new IllegalStateException("Seat is not held");
        }

        // mark seat booked
        seat.setBookingShowSeatStatus(BookingShowSeatStatus.BOOKED);
        seatRepository.save(seat);

        // set payment id and status on booking
        booking.setPaymentId(paymentId);
        booking.setStatus(BookingShowSeatStatus.BOOKED);
        booking.setAmount(java.math.BigDecimal.ZERO); // optionally set amount from show
        bookingRepository.save(booking);

        // decrement show.availableSeats with optimistic retry
        int attempt = 0;
        Show show = null;
        while (true) {
            try {
                show = showRepository.findById(booking.getShowId())
                        .orElseThrow(() -> new IllegalStateException("Show does not exist"));

                int avail = show.getAvailableSeats();
                if (avail <= 0) {
                    throw new IllegalStateException("No available seats");
                }
                show.setAvailableSeats(avail - 1);
                showRepository.save(show);
                break;
            } catch (OptimisticLockException ole) {
                attempt++;
                if (attempt >= MAX_RETRIES) {
                    throw new IllegalStateException("Failed to update show availability due to concurrent updates");
                }
                try {
                    Thread.sleep(50L * attempt);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new IllegalStateException("Interrupted while retrying availability update");
                }
            }
        }

        // generate ticket PDF and send email to user (best-effort)
        try {
            User user = userRepository.findById(booking.getUserId())
                    .orElseThrow(() -> new IllegalStateException("User not found"));

            byte[] pdf = ticketService.generateTicketPdf(booking, show, user.getName());
            emailService.sendBookingTicketEmail(user.getEmail(), user.getName(), booking, pdf);
        } catch (Exception ex) {
            // log and continue - booking already confirmed
            // Using system out because EmailService has its own logger
            System.err.println("Failed to generate/send ticket: " + ex.getMessage());
        }
    }
}