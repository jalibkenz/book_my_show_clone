package in.kenz.bookmyshow.theatre.controller;

import in.kenz.bookmyshow.screenandseat.dto.ShowDto;
import in.kenz.bookmyshow.screenandseat.mapper.ShowMapper;
import in.kenz.bookmyshow.screenandseat.repository.ShowRepository;
import in.kenz.bookmyshow.screenandseat.dto.SeatDto;
import in.kenz.bookmyshow.screenandseat.mapper.SeatMapper;
import in.kenz.bookmyshow.screenandseat.repository.SeatRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/theatres")
@RequiredArgsConstructor
@Tag(name = "Theatre Module")
public class TheatreController {

    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;
    private final ShowMapper showMapper;
    private final SeatMapper seatMapper;
    private final in.kenz.bookmyshow.theatre.service.TheatreService theatreService;

    @PostMapping("/create")
    public ResponseEntity<in.kenz.bookmyshow.theatre.entity.Theatre> createTheatre(@RequestBody in.kenz.bookmyshow.theatre.dto.SignupTheatreRequest request) {
        return ResponseEntity.status(201).body(theatreService.createTheatre(request));
    }

    @GetMapping("/{theatreId}/shows")
    @Cacheable(value = "showsByTheatre", key = "#theatreId + ':' + #pageable.pageNumber + ':' + #pageable.pageSize")
    public ResponseEntity<Page<ShowDto>> listShows(@PathVariable UUID theatreId, Pageable pageable) {
        var page = showRepository.findByTheatreId(theatreId, pageable);
        var dtoPage = page.map(showMapper::toDto);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/{theatreId}/shows/{showId}/seats")
    public ResponseEntity<List<SeatDto>> listSeatsForShow(
            @PathVariable UUID theatreId,
            @PathVariable UUID showId
    ) {
        var show = showRepository.findById(showId).orElse(null);
        if (show == null) {
            return ResponseEntity.notFound().build();
        }
        if (!theatreId.equals(show.getTheatreId())) {
            return ResponseEntity.badRequest().build();
        }
        var seats = seatRepository.findByShowId(showId);
        var dto = seats.stream().map(seatMapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dto);
    }
}
