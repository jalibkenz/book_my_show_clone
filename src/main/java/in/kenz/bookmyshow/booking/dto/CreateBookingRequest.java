package in.kenz.bookmyshow.booking.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateBookingRequest {

    @NotNull
    private UUID userId;

    @NotBlank
    private String seatNumber;
}

