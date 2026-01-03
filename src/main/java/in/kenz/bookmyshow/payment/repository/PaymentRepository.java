package in.kenz.bookmyshow.payment.repository;

import in.kenz.bookmyshow.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    // Used later during payment verification
    Optional<Payment> findByGatewayOrderId(String gatewayOrderId);
}