package in.kenz.bookmyshow.booking.controller;

import in.kenz.bookmyshow.booking.entity.Booking;
import in.kenz.bookmyshow.booking.repository.BookingRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
@Tag(name = "Booking Module")
public class BookingQueryController {

    private final BookingRepository bookingRepository;

    @GetMapping("/{bookingId}")
    public ResponseEntity<Booking> getBooking(@PathVariable UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

