package in.kenz.bookmyshow.payment.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/payments/config")
@RequiredArgsConstructor
@Tag(name = "Payment Module")
@CrossOrigin
public class PaymentConfigController {

    @Value("${razorpay.key-id}")
    private String razorpayKeyId;

    @GetMapping
    public Map<String, String> getPaymentConfig() {
        return Map.of(
                "razorpayKeyId", razorpayKeyId
        );
    }
}