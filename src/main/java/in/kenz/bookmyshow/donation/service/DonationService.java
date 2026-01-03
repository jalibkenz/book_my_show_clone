package in.kenz.bookmyshow.donation.service;

import in.kenz.bookmyshow.donation.dto.CreateDonationRequest;
import in.kenz.bookmyshow.donation.dto.DonationPaymentResponse;
import in.kenz.bookmyshow.donation.entity.Donation;
import in.kenz.bookmyshow.donation.enums.DonationStatus;
import in.kenz.bookmyshow.donation.repository.DonationRepository;
import in.kenz.bookmyshow.payment.entity.Payment;
import in.kenz.bookmyshow.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DonationService {

    private final DonationRepository donationRepository;
    private final PaymentService paymentService;
    private static final String DEFAULT_CURRENCY = "INR";
    /**
     * Creates donation + payment intent + Razorpay order.
     */
    @Transactional
    public DonationPaymentResponse createDonationWithPayment(
            CreateDonationRequest request
    ) {

        if (request.getAmount() == null || request.getAmount() <= 0) {
            throw new IllegalArgumentException("Invalid donation amount");
        }

        // 1. Create donation intent
        Donation donation = new Donation();
        donation.setName(request.getName());
        donation.setEmail(request.getEmail());
        donation.setMobile(request.getMobile());
        donation.setGender(request.getGender());
        donation.setAmount(request.getAmount());
        donation.setDonationStatus(DonationStatus.CREATED);
        donation.setCreatedAt(LocalDateTime.now());

        donationRepository.save(donation);

        // 2. Create Razorpay payment
        Payment payment = paymentService.createRazorpayPayment(
                donation.getAmount(),
                DEFAULT_CURRENCY
        );

        // 3. Link donation -> payment
        donation.setPaymentId(payment.getId());
        donationRepository.save(donation);

        // 4. Return checkout details
        return new DonationPaymentResponse(
                donation.getId(),
                payment.getId(),
                payment.getGatewayOrderId(),
                payment.getAmount(),
                payment.getCurrency()
        );
    }
}