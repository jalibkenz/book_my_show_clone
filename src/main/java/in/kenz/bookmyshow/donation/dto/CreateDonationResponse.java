package in.kenz.bookmyshow.donation.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class CreateDonationResponse {

    private UUID donationId;
    private Integer amount;
    private String status;
}