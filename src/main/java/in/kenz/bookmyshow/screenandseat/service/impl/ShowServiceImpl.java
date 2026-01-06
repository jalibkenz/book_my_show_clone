package in.kenz.bookmyshow.screenandseat.service.impl;

import in.kenz.bookmyshow.screenandseat.entity.Seat;
import in.kenz.bookmyshow.booking.enums.BookingShowSeatStatus;
import in.kenz.bookmyshow.screenandseat.repository.SeatRepository;
import in.kenz.bookmyshow.screenandseat.dto.CreateShowRequest;
import in.kenz.bookmyshow.screenandseat.entity.Show;
import in.kenz.bookmyshow.screenandseat.enums.ConductingStatus;
import in.kenz.bookmyshow.screenandseat.repository.ShowRepository;
import in.kenz.bookmyshow.screenandseat.service.ShowService;
import in.kenz.bookmyshow.theatre.repository.TheatreRepository;
import in.kenz.bookmyshow.movie.repository.MovieRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final TheatreRepository theatreRepository;
    private final SeatRepository seatRepository;
    private final MovieRepository movieRepository;

    @Transactional
    @Override
    public Show createShow(CreateShowRequest request) {

        // validate theatre exists
        if (!theatreRepository.existsById(request.getTheatreId())) {
            throw new IllegalArgumentException("Theatre does not exist");
        }

        // validate movie exists
        if (!movieRepository.existsById(request.getMovieId())) {
            throw new IllegalArgumentException("Movie does not exist");
        }

        // 1️⃣ Validate time window
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new IllegalArgumentException(
                    "End time must be after start time"
            );
        }

        // 2️⃣ Validate seat layout consistency
        int calculatedSeats =
                request.getSeatRows() * request.getSeatsPerRow();

        if (!Integer.valueOf(calculatedSeats).equals(request.getTotalSeats())) {
            throw new IllegalArgumentException(
                    "Total seats must equal seatRows × seatsPerRow"
            );
        }

        // 3️⃣ Overlap check (KEEP THIS)
        boolean overlapExists =
                showRepository.existsOverlappingShowOnScreen(
                        request.getTheatreId(),
                        request.getScreenName(),
                        request.getStartTime(),
                        request.getEndTime()
                );

        if (overlapExists) {
            throw new IllegalStateException(
                    "Another show exists on this screen during the same time window"
            );
        }

        // 4️⃣ Create show
        Show show = Show.builder()
                .theatreId(request.getTheatreId())
                .movieId(request.getMovieId())
                .screenName(request.getScreenName())
                .screenSeats(request.getTotalSeats())     // ✅ FIXED
                .availableSeats(request.getTotalSeats()) // ✅ FIXED
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .basePrice(request.getBasePrice())
                .conductingStatus(ConductingStatus.SCHEDULED)
                .build();

        Show savedShow = showRepository.save(show);

        // 5️⃣ Generate seats
        generateSeats(
                savedShow.getId(),
                request.getSeatRows(),
                request.getSeatsPerRow()
        );

        return savedShow;
    }

    /**
     * Generates seats for a show.
     * OWNED by ShowService.
     */
    private void generateSeats(
            UUID showId,
            int rows,
            int seatsPerRow
    ) {
        for (char row = 'A'; row < 'A' + rows; row++) {
            for (int seat = 1; seat <= seatsPerRow; seat++) {

                seatRepository.save(
                        Seat.builder()
                                .showId(showId)
                                .seatNumber(row + String.valueOf(seat)) // A1, A2, B7
                                .bookingShowSeatStatus(
                                        BookingShowSeatStatus.AVAILABLE
                                )
                                .build()
                );
            }
        }
    }
}