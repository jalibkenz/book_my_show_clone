package in.kenz.bookmyshow.booking.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitiateBookingPaymentResponse {
    private String razorpayOrderId;
    private Integer amount;
    private String currency;
}

