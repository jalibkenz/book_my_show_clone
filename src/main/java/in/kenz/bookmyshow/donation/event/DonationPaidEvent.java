package in.kenz.bookmyshow.donation.event;

import java.util.UUID;

public class DonationPaidEvent {

    private final UUID donationId;

    public DonationPaidEvent(UUID donationId) {
        this.donationId = donationId;
    }

    public UUID getDonationId() {
        return donationId;
    }
}