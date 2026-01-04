package in.kenz.bookmyshow.payment.controller;

import in.kenz.bookmyshow.payment.dto.PaymentVerificationRequest;
import in.kenz.bookmyshow.payment.service.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Tag(name = "Payment Module")
@CrossOrigin
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