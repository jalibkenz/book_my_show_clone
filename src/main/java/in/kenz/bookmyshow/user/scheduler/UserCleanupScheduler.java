package in.kenz.bookmyshow.user.scheduler;

import in.kenz.bookmyshow.user.entity.User;
import in.kenz.bookmyshow.user.enums.ProfileStatus;
import in.kenz.bookmyshow.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserCleanupScheduler {

    private final UserRepository userRepository;

    @Transactional
    @Scheduled(fixedDelay = 1_800_000) // repeat every 30 minutes = 30 × 60 × 1000 = 1,800,000 ms.
    public void purgeSoftDeletedUsers() {

        LocalDateTime cutoff =
                LocalDateTime.now().minusMinutes(1); // check if there is any profile deleted BEFORE 1 minute from NOW

        List<User> expiredUsers =
                userRepository.findByProfileStatusAndDeletedAtBefore(
                        ProfileStatus.DELETED,
                        cutoff
                );

        if (expiredUsers.isEmpty()) {
            return;
        }

        log.warn("Purging {} soft-deleted users", expiredUsers.size());
        userRepository.deleteAll(expiredUsers);
    }
}