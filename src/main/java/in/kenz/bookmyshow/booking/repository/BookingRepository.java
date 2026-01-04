package in.kenz.bookmyshow.booking.repository;

import in.kenz.bookmyshow.booking.entity.Booking;
import in.kenz.bookmyshow.booking.enums.BookingShowSeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("select b from Booking b where b.status = :status and b.createdAt < :cutoff")
    List<Booking> findByStatusAndCreatedAtBefore(BookingShowSeatStatus status, OffsetDateTime cutoff);

    Optional<Booking> findByPaymentInternalId(UUID paymentInternalId);
}
