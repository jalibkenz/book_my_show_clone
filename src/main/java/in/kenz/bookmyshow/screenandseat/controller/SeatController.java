package in.kenz.bookmyshow.screenandseat.controller;

import in.kenz.bookmyshow.screenandseat.entity.Seat;
import in.kenz.bookmyshow.screenandseat.repository.SeatRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/seats")
@RequiredArgsConstructor
@Tag(name = "ScreenAndSeat Module")
public class SeatController {

    private final SeatRepository repository;

    @GetMapping("/shows/{showId}")
    public List<Seat> listSeats(
            @PathVariable UUID showId
    ) {
        return repository.findByShowId(showId);
    }
}