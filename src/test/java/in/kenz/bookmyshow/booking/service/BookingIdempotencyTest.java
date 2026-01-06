package in.kenz.bookmyshow.booking.service;

import in.kenz.bookmyshow.booking.entity.Booking;
import in.kenz.bookmyshow.booking.enums.BookingShowSeatStatus;
import in.kenz.bookmyshow.booking.repository.BookingRepository;
import in.kenz.bookmyshow.booking.service.impl.BookingServiceImpl;
import in.kenz.bookmyshow.notification.email.service.EmailService;
import in.kenz.bookmyshow.screenandseat.entity.Seat;
import in.kenz.bookmyshow.screenandseat.entity.Show;
import in.kenz.bookmyshow.screenandseat.repository.SeatRepository;
import in.kenz.bookmyshow.screenandseat.repository.ShowRepository;
import in.kenz.bookmyshow.user.entity.User;
import in.kenz.bookmyshow.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingIdempotencyTest {

    @Mock
    private SeatRepository seatRepository;
    @Mock
    private ShowRepository showRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private TicketService ticketService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private UUID showId;
    private UUID bookingId;
    private String paymentId;

    @BeforeEach
    void setUp() {
        showId = UUID.randomUUID();
        bookingId = UUID.randomUUID();
        paymentId = "pay_123";
    }

    @Test
    void payBooking_shouldBeIdempotent_whenAlreadyBookedWithSamePaymentId() {
        // Arrange
        Booking booking = Booking.builder()
                .id(bookingId)
                .showId(showId)
                .status(BookingShowSeatStatus.BOOKED)
                .paymentId(paymentId)
                .build();

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));

        // Act
        bookingService.payBooking(showId, bookingId, paymentId);

        // Assert
        verify(bookingRepository, times(1)).findById(bookingId);
        verify(bookingRepository, never()).save(any());
        verify(seatRepository, never()).lockSeatForBooking(any(), any());
    }

    @Test
    void payBooking_shouldSucceed_whenInHeldState() {
        // Arrange
        Booking booking = Booking.builder()
                .id(bookingId)
                .showId(showId)
                .seatNumber("A1")
                .userId(UUID.randomUUID())
                .status(BookingShowSeatStatus.HELD)
                .amount(BigDecimal.valueOf(100))
                .build();

        Seat seat = Seat.builder()
                .showId(showId)
                .seatNumber("A1")
                .bookingId(bookingId)
                .bookingShowSeatStatus(BookingShowSeatStatus.HELD)
                .build();

        Show show = new Show();
        show.setId(showId);
        show.setAvailableSeats(10);

        User user = new User();
        user.setEmail("test@example.com");
        user.setName("Test User");

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(seatRepository.lockSeatForBooking(showId, "A1")).thenReturn(Optional.of(seat));
        when(showRepository.findById(showId)).thenReturn(Optional.of(show));
        when(userRepository.findById(any())).thenReturn(Optional.of(user));

        // Act
        bookingService.payBooking(showId, bookingId, paymentId);

        // Assert
        verify(bookingRepository).save(argThat(b -> 
            b.getStatus() == BookingShowSeatStatus.BOOKED && 
            paymentId.equals(b.getPaymentId()) &&
            BigDecimal.valueOf(100).compareTo(b.getAmount()) == 0 // verify amount is preserved
        ));
        verify(seatRepository).save(argThat(s -> s.getBookingShowSeatStatus() == BookingShowSeatStatus.BOOKED));
        verify(showRepository).save(argThat(s -> s.getAvailableSeats() == 9));
    }
}
