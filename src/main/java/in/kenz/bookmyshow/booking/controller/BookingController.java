package in.kenz.bookmyshow.booking.controller;

import in.kenz.bookmyshow.booking.dto.CreateBookingRequest;
import in.kenz.bookmyshow.booking.dto.PayBookingRequest;
import in.kenz.bookmyshow.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking Module")
@CrossOrigin
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/shows/{showId}")
    @Operation(summary = "bookSeat")
    public ResponseEntity<UUID> bookSeat(
            @PathVariable UUID showId,
            @RequestBody @Valid CreateBookingRequest request
    ) {
        UUID bookingId = bookingService.bookSeat(showId, request.getSeatNumber(), request.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingId);
    }

    @PostMapping("/{bookingId}/shows/{showId}/pay")
    @Operation(summary = "payBooking")
    public ResponseEntity<Void> payBooking(
            @PathVariable UUID showId,
            @PathVariable UUID bookingId,
            @RequestBody @Valid PayBookingRequest request
    ) {
        bookingService.payBooking(showId, bookingId, request.getPaymentId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
