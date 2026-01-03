package in.kenz.bookmyshow.payment.controller;

import in.kenz.bookmyshow.payment.dto.PaymentVerificationRequest;
import in.kenz.bookmyshow.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyPayment(
            @RequestBody PaymentVerificationRequest request
    ) {
        paymentService.verifyRazorpayPayment(request);
        return ResponseEntity.ok().build();
    }
}