package in.kenz.bookmyshow.payment.enums;


public enum PaymentStatus {
    CREATED,     // Order created, payment not done yet
    SUCCESS,     // Payment verified successfully
    FAILED       // Payment failed or verification failed
}
