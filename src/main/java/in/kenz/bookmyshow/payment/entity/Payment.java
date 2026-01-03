package in.kenz.bookmyshow.payment.entity;

import in.kenz.bookmyshow.payment.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue
    private UUID id;   // Internal payment reference

    // Gateway info
    private String gateway;               // e.g. RAZORPAY
    private String gatewayOrderId;         // Razorpay order_id
    private String gatewayPaymentId;       // Razorpay payment_id

    // Amount info
    private Integer amount;                // INR
    private String currency;               // INR

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    // Audit timestamps
    private LocalDateTime createdAt;        // Order created
    private LocalDateTime completedAt;      // Payment success/failure

    /* getters & setters */
}
