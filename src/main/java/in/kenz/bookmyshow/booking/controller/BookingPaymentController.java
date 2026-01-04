package in.kenz.bookmyshow.booking.controller;

import in.kenz.bookmyshow.booking.dto.InitiateBookingPaymentResponse;
import in.kenz.bookmyshow.booking.entity.Booking;
import in.kenz.bookmyshow.booking.repository.BookingRepository;
import in.kenz.bookmyshow.booking.service.BookingService;
import in.kenz.bookmyshow.payment.service.PaymentService;
import in.kenz.bookmyshow.payment.entity.Payment;
import in.kenz.bookmyshow.booking.service.TicketService;
import in.kenz.bookmyshow.notification.email.service.EmailService;
import in.kenz.bookmyshow.show.repository.ShowRepository;
import in.kenz.bookmyshow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bookings/{bookingId}")
@RequiredArgsConstructor
public class BookingPaymentController {

    private final BookingRepository bookingRepository;
    private final PaymentService paymentService;
    private final TicketService ticketService;
    private final EmailService emailService;
    private final ShowRepository showRepository;
    private final UserRepository userRepository;

    @PostMapping("/pay/initiate")
    public ResponseEntity<InitiateBookingPaymentResponse> initiatePayment(@PathVariable UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() != in.kenz.bookmyshow.booking.enums.BookingShowSeatStatus.HELD) {
            throw new IllegalStateException("Booking not in HELD state");
        }

        // create payment (amount in INR integer)
        Integer amount = booking.getAmount().intValue();
        Payment payment = paymentService.createRazorpayPayment(amount, "INR");

        // attach to booking
        booking.setPaymentInternalId(payment.getId());
        bookingRepository.save(booking);

        InitiateBookingPaymentResponse res = new InitiateBookingPaymentResponse();
        res.setRazorpayOrderId(payment.getGatewayOrderId());
        res.setAmount(payment.getAmount());
        res.setCurrency(payment.getCurrency());

        return ResponseEntity.status(201).body(res);
    }

    @PostMapping("/resend-ticket")
    public ResponseEntity<Void> resendTicket(@PathVariable UUID bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        if (booking.getStatus() != in.kenz.bookmyshow.booking.enums.BookingShowSeatStatus.BOOKED) {
            throw new IllegalStateException("Only confirmed bookings (BOOKED) can have tickets re-sent");
        }

        var user = userRepository.findById(booking.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found for booking"));

        var show = showRepository.findById(booking.getShowId())
                .orElseThrow(() -> new IllegalArgumentException("Show not found for booking"));

        // generate ticket PDF and email
        byte[] pdf = ticketService.generateTicketPdf(booking, show, user.getName());
        emailService.sendBookingTicketEmail(user.getEmail(), user.getName(), booking, pdf);

        return ResponseEntity.noContent().build();
    }
}
