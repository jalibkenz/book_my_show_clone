package in.kenz.bookmyshow.notification.email.listener;

import in.kenz.bookmyshow.notification.email.service.EmailService;
import in.kenz.bookmyshow.theatre.event.TheatreCreatedEvent;
import in.kenz.bookmyshow.theatre.event.TheatreProfileUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Slf4j
@Component
@RequiredArgsConstructor
public class TheatreEmailEventListener {

    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTheatreCreated(TheatreCreatedEvent event) {
        log.info("TheatreCreatedEvent received AFTER_COMMIT for {}", event.getEmail());
        emailService.sendTheatreCreatedEmail(event);
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onTheatreUpdated(TheatreProfileUpdatedEvent event) {
        log.info("TheatreProfileUpdatedEvent received AFTER_COMMIT for {}", event.getOldEmail());
        emailService.sendTheatreProfileUpdatedEmail(event);
    }
}