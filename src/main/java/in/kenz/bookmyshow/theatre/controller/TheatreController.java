package in.kenz.bookmyshow.theatre.controller;

import in.kenz.bookmyshow.show.entity.Show;
import in.kenz.bookmyshow.show.repository.ShowRepository;
import in.kenz.bookmyshow.seat.entity.Seat;
import in.kenz.bookmyshow.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/theatres/{theatreId}")
@RequiredArgsConstructor
public class TheatreController {

    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    @GetMapping("/shows")
    public ResponseEntity<List<Show>> listShows(@PathVariable UUID theatreId) {
        return ResponseEntity.ok(showRepository.findByTheatreId(theatreId));
    }

    @GetMapping("/shows/{showId}/seats")
    public ResponseEntity<List<Seat>> listSeatsForShow(
            @PathVariable UUID theatreId,
            @PathVariable UUID showId
    ) {
        // Basic sanity: verify show belongs to theatre
        Show show = showRepository.findById(showId).orElse(null);
        if (show == null) {
            return ResponseEntity.notFound().build();
        }
        if (!theatreId.equals(show.getTheatreId())) {
            return ResponseEntity.badRequest().build();
        }
        List<Seat> seats = seatRepository.findByShowId(showId);
        return ResponseEntity.ok(seats);
    }
}
