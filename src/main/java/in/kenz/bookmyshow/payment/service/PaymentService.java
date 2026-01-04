package in.kenz.bookmyshow.payment.service;

import com.razorpay.Order;
import in.kenz.bookmyshow.donation.entity.Donation;
import in.kenz.bookmyshow.donation.enums.DonationStatus;
import in.kenz.bookmyshow.donation.event.DonationPaidEvent;
import in.kenz.bookmyshow.donation.repository.DonationRepository;
import in.kenz.bookmyshow.payment.dto.PaymentVerificationRequest;
import in.kenz.bookmyshow.payment.entity.Payment;
import in.kenz.bookmyshow.payment.enums.PaymentStatus;
import in.kenz.bookmyshow.payment.gateway.RazorpayGatewayService;
import in.kenz.bookmyshow.payment.repository.PaymentRepository;
import in.kenz.bookmyshow.booking.repository.BookingRepository;
import in.kenz.bookmyshow.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final RazorpayGatewayService razorpayGatewayService;
    private final DonationRepository donationRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final BookingRepository bookingRepository;
    private final BookingService bookingService;


    @Value("${razorpay.key-secret}")
    private String razorpayKeySecret;

    /**
     * Core payment intent creation (gateway-agnostic).
     * This is your PAYMENT LEDGER entry point.
     */
    public Payment createPayment(Integer amount, String currency, String gateway) {

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setGateway(gateway);
        payment.setPaymentStatus(PaymentStatus.CREATED);
        payment.setCreatedAt(LocalDateTime.now());

        return paymentRepository.save(payment);
    }

    /**
     * Razorpay-specific payment intent + order creation.
     * Builds on top of createPayment().
     */
    public Payment createRazorpayPayment(Integer amount, String currency) {

        // 1. Create internal payment intent
        Payment payment = createPayment(amount, currency, "RAZORPAY");

        // 2. Create Razorpay order
        Order order = razorpayGatewayService.createOrder(
                amount,
                currency,
                payment.getId().toString() // receipt = payment intent ID
        );

        // 3. Persist gateway order reference
        payment.setGatewayOrderId(order.get("id"));
        return paymentRepository.save(payment);
    }



    @Transactional
    public void verifyRazorpayPayment(PaymentVerificationRequest request) {

        // 1. Build signature payload (ORDER_ID|PAYMENT_ID)
        String payload =
                request.getRazorpayOrderId() + "|" +
                        request.getRazorpayPaymentId();

        String generatedSignature = hmacSha256(
                payload,
                razorpayKeySecret
        );

        if (!generatedSignature.equalsIgnoreCase(request.getRazorpaySignature())) {
            log.error("Signature verification failed. Expected: {}, Received: {}", generatedSignature, request.getRazorpaySignature());
            throw new IllegalStateException("Payment signature verification failed");
        }

        // 2. Update payment
        Payment payment = paymentRepository
                .findByGatewayOrderId(request.getRazorpayOrderId())
                .orElseThrow(() ->
                        new IllegalArgumentException("Payment not found for order"));

        payment.setGatewayPaymentId(request.getRazorpayPaymentId());
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setCompletedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // 3. Attempt to resolve donation (existing flow)
        donationRepository.findByPaymentId(payment.getId()).ifPresent(donation -> {
            log.info("DONATION PAYMENT VERIFIED, EVENT PUBLISHED: {}", donation.getId());
            donation.setDonationStatus(DonationStatus.SUCCESS);
            donation.setDonatedAt(LocalDateTime.now());
            donationRepository.save(donation);
            eventPublisher.publishEvent(new DonationPaidEvent(donation.getId()));
        });

        // 4. Attempt to resolve a booking associated with this payment (new flow)
        bookingRepository.findByPaymentInternalId(payment.getId()).ifPresent(booking -> {
            log.info("BOOKING PAYMENT VERIFIED: {} -> marking booking as paid", booking.getId());
            // mark booking as paid via bookingService
            bookingService.payBooking(booking.getShowId(), booking.getId(), payment.getGatewayPaymentId());
        });
    }
    //helper for verifyRazorpayPayment()
    private String hmacSha256(String data, String secret) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec key =
                    new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(key);
            byte[] raw = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(raw);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}