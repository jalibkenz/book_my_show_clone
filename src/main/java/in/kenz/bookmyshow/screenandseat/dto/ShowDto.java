package in.kenz.bookmyshow.screenandseat.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ShowDto {
    private UUID id;
    private UUID theatreId;
    private UUID movieId;
    private String screenName;
    private Integer screenSeats;
    private Integer availableSeats;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal basePrice;
    private String conductingStatus;
}

