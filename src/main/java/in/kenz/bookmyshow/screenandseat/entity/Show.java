package in.kenz.bookmyshow.screenandseat.entity;

import in.kenz.bookmyshow.screenandseat.enums.ConductingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "shows",
        indexes = {
                @Index(
                        name = "idx_show_theatre_screen_time",
                        columnList = "theatre_id,screen_name,start_time,end_time"
                )
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Show {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /** Theatre reference (ID only) */
    @Column(name = "theatre_id", nullable = false)
    private UUID theatreId;

    /** Movie reference (ID only) */
    @Column(nullable = false)
    private UUID movieId;

    /** Screen identifier (mutable, human-readable) */
    @Column(name = "screen_name", nullable = false, length = 50)
    private String screenName;

    @Column(nullable = false)
    private Integer screenSeats;

    @Column(nullable = false)
    private Integer availableSeats;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal basePrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConductingStatus conductingStatus;

    @Version
    private Long version;
}