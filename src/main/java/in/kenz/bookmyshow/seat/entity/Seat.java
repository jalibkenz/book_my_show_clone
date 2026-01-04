package in.kenz.bookmyshow.seat.entity;

import in.kenz.bookmyshow.booking.enums.BookingShowSeatStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(
        name = "booking_show_seats",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_show_seat",
                        columnNames = {"show_id", "seat_number"}
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "show_id", nullable = false)
    private UUID showId;

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber; // A7, B2

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingShowSeatStatus bookingShowSeatStatus;

    @Column(name = "booking_id")
    private UUID bookingId;
}