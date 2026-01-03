package in.kenz.bookmyshow.donation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class DonationPaymentResponse {

    private UUID donationId;
    private UUID paymentId;          // payment intent
    private String razorpayOrderId;  // gateway order
    private Integer amount;
    private String currency;
}