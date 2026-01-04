package in.kenz.bookmyshow.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PaymentConfigController {

    @Value("${razorpay.key-id}")
    private String razorpayKeyId;

    @GetMapping("/payments/config")
    public Map<String, String> getPaymentConfig() {
        return Map.of(
                "razorpayKeyId", razorpayKeyId
        );
    }
}