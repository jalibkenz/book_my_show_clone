package in.kenz.bookmyshow.donation.listener;

import in.kenz.bookmyshow.donation.entity.Donation;
import in.kenz.bookmyshow.donation.event.DonationPaidEvent;
import in.kenz.bookmyshow.donation.repository.DonationRepository;
import in.kenz.bookmyshow.donation.service.DonationCertificatePdfService;
import in.kenz.bookmyshow.notification.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Slf4j
@Component
@RequiredArgsConstructor
public class DonationPaidEventListener {

    private final DonationRepository donationRepository;
    private final DonationCertificatePdfService pdfService;
    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(DonationPaidEvent event) {
        try {
            log.info("DONATION PAID EVENT RECEIVED for donationId={}", event.getDonationId());

            Donation donation = donationRepository.findById(event.getDonationId())
                    .orElseThrow();

            byte[] pdf = pdfService.generate(donation);

            emailService.sendDonationSuccessMail(donation, pdf);

            log.info("DONATION EMAIL SENT for donationId={}", donation.getId());

        } catch (Exception ex) {
            log.error("FAILED TO SEND DONATION EMAIL for donationId={}",
                    event.getDonationId(), ex);
        }
    }
}