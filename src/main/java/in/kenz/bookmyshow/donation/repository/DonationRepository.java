package in.kenz.bookmyshow.donation.repository;

import in.kenz.bookmyshow.donation.entity.Donation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DonationRepository extends JpaRepository<Donation, UUID> {
    Optional<Donation>findByPaymentId(UUID paymentId);
}