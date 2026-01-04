package in.kenz.bookmyshow.seat.controller;

import in.kenz.bookmyshow.seat.entity.Seat;
import in.kenz.bookmyshow.seat.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/shows/{showId}/seats")
@RequiredArgsConstructor
public class SeatController {

    private final SeatRepository repository;

    @GetMapping
    public List<Seat> listSeats(
            @PathVariable UUID showId
    ) {
        return repository.findByShowId(showId);
    }
}