package in.kenz.bookmyshow.donation.controller;

import in.kenz.bookmyshow.donation.dto.CreateDonationRequest;
import in.kenz.bookmyshow.donation.dto.DonationPaymentResponse;
import in.kenz.bookmyshow.donation.service.DonationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/donations")
@RequiredArgsConstructor
@Tag(name = "Donation Module")
@CrossOrigin
public class DonationController {

    private final DonationService donationService;

    /**
     * Donation + Razorpay order creation
     */
    @PostMapping("/pay")
    @Operation(summary = "payDonation")
    public ResponseEntity<DonationPaymentResponse> payDonation(
            @Valid @RequestBody CreateDonationRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(donationService.createDonationWithPayment(request));
    }
}