package in.kenz.bookmyshow.booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "show_id", nullable = false)
    private UUID showId;

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "amount", nullable = false)
    private java.math.BigDecimal amount;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "payment_internal_id")
    private UUID paymentInternalId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private in.kenz.bookmyshow.booking.enums.BookingShowSeatStatus status;
}
