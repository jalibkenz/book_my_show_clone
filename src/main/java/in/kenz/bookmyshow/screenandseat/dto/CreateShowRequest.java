package in.kenz.bookmyshow.screenandseat.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CreateShowRequest {

    /** Theatre reference */
    @NotNull
    private UUID theatreId;

    /** Movie reference */
    @NotNull
    private UUID movieId;

    /** Screen identifier (mutable, human-readable) */
    @NotBlank
    private String screenName;

    /** Seat layout definition */
    @NotNull
    @Min(1)
    @Max(26) // simple guard; change if supporting >26 rows
    private Integer seatRows;        // e.g. 10 → A to J

    @NotNull
    @Min(1)
    private Integer seatsPerRow;     // e.g. 12 → A1–A12

    /** Derived capacity (validated in service) */
    @NotNull
    @Min(1)
    private Integer totalSeats;

    /** Show timing */
    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;

    /** Pricing */
    @NotNull
    @DecimalMin("0.0")
    private BigDecimal basePrice;
}