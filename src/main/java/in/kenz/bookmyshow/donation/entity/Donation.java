package in.kenz.bookmyshow.donation.entity;

import in.kenz.bookmyshow.donation.enums.DonationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "donations")
public class Donation {

    @Id
    @GeneratedValue
    private UUID id;   // Receipt Number

    // Donor details
    private String name;
    private String email;
    private String mobile;
    private String gender;

    // Donation details
    private Integer amount;

    // Internal payment reference (gateway-agnostic)
    private UUID paymentId;

    @Enumerated(EnumType.STRING)
    private DonationStatus donationStatus;

    // Timestamps
    private LocalDateTime createdAt;   // Donation intent created
    private LocalDateTime donatedAt;   // Payment success time
}