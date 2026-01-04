package in.kenz.bookmyshow.seat.repository;

import in.kenz.bookmyshow.seat.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SeatRepository
        extends JpaRepository<Seat, UUID> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
        select s
        from Seat s
        where s.showId = :showId
          and s.seatNumber = :seatNumber
    """)
    Optional<Seat> lockSeatForBooking(
            UUID showId,
            String seatNumber
    );

    List<Seat> findByShowId(UUID showId);

    Optional<Seat> findByShowIdAndSeatNumber(UUID showId, String seatNumber);
}