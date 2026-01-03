package in.kenz.bookmyshow.notification.email.listener;

import in.kenz.bookmyshow.notification.email.service.EmailService;
import in.kenz.bookmyshow.user.event.UserCreatedEvent;
import lombok.RequiredArgsConstructor;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserCreatedEmailListener {

    private final EmailService emailService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(UserCreatedEvent event) {
        emailService.sendUserCreatedMail(
                event.getEmail(),
                event.getName()
        );
    }
}
