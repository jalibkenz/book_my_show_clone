package in.kenz.bookmyshow.booking.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayBookingRequest {

    @NotBlank
    private String paymentId;
}

