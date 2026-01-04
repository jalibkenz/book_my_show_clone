package in.kenz.bookmyshow.payment.controller;

import in.kenz.bookmyshow.booking.dto.PayBookingRequest;
import in.kenz.bookmyshow.booking.repository.BookingRepository;
import in.kenz.bookmyshow.booking.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Booking Module")
@CrossOrigin
public class PaymentsController {

    private final BookingService bookingService;
    private final BookingRepository bookingRepository;

    @PostMapping("/bookings/{bookingId}/pay")
    public ResponseEntity<Void> payBooking(
            @PathVariable UUID bookingId,
            @RequestBody @Valid PayBookingRequest request
    ) {
        // fetch booking to obtain showId
        var booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        UUID showId = booking.getShowId();
        bookingService.payBooking(showId, bookingId, request.getPaymentId());
        return ResponseEntity.noContent().build();
    }
}
