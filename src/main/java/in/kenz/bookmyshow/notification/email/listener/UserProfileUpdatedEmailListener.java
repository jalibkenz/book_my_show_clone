package in.kenz.bookmyshow.notification.email.listener;

import in.kenz.bookmyshow.notification.email.service.EmailService;
import in.kenz.bookmyshow.user.event.UserProfileUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;
@Slf4j
@Component
@RequiredArgsConstructor
public class UserProfileUpdatedEmailListener {

    private final EmailService emailService;


    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserProfileUpdatedEvent event) {
        log.info("UserProfileUpdatedEvent received AFTER_COMMIT for {}", event.getEmail());
        emailService.sendUserProfileUpdatedMail(
                event

        );
    }
}