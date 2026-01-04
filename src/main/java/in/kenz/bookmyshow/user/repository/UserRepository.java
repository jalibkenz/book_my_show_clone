package in.kenz.bookmyshow.user.repository;

import in.kenz.bookmyshow.user.entity.User;
import in.kenz.bookmyshow.user.enums.ProfileStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String username);
    boolean existsByUsernameAndIdNot(String username, UUID userId);
    boolean existsByEmailAndIdNot(String email, UUID userId);
    boolean existsByEmail(String email);
    List<User> findByProfileStatusAndDeletedAtBefore(ProfileStatus status,LocalDateTime cutoff);
}
