package in.kenz.bookmyshow.user.service.impl;

import in.kenz.bookmyshow.common.exception.DuplicateResourceException;
import in.kenz.bookmyshow.common.exception.ResourceNotFoundException;
import in.kenz.bookmyshow.user.dto.SignupRequest;
import in.kenz.bookmyshow.user.dto.UpdateRequest;
import in.kenz.bookmyshow.user.entity.User;
import in.kenz.bookmyshow.user.enums.ProfileStatus;
import in.kenz.bookmyshow.user.event.UserCreatedEvent;
import in.kenz.bookmyshow.user.event.UserProfileUpdatedEvent;
import in.kenz.bookmyshow.user.repository.UserRepository;
import in.kenz.bookmyshow.user.service.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

import static org.springframework.util.StringUtils.hasText;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UserServiceImpl(UserRepository userRepository, ApplicationEventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    @Override
    public User createUser(SignupRequest signupRequest) {
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new DuplicateResourceException("Username is already in use");
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new DuplicateResourceException("Username is already in use");
        }


        User user = User.builder()
                .name(signupRequest.getName())
                .username(signupRequest.getUsername())
                .password(signupRequest.getPassword())
                .email(signupRequest.getEmail())
                .build();
        userRepository.save(user);
        // 🔔 Trigger email event
        eventPublisher.publishEvent(
                new UserCreatedEvent(
                        user.getEmail(),
                        user.getName()
                )
        );
        return user;
    }

    @Override
    public User fetchUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User ID is invalid"));
    }

    @Transactional
    public User updateUser(UUID userId, UpdateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // ======================
        // Capture OLD values
        // ======================
        String oldName = user.getName();
        String oldEmergencyContactName = user.getEmergencyContactName();
        String oldEmergencyContactEmail = user.getEmergencyContactEmail();
        String oldEmergencyContactMobile = user.getEmergencyContactMobile();

        // ======================
        // USERNAME
        // ======================
        if (hasText(request.getUsername())
                && !request.getUsername().equals(user.getUsername())) {

            if (userRepository.existsByUsernameAndIdNot(
                    request.getUsername(), userId)) {
                throw new DuplicateResourceException("Username already exists");
            }

            user.setUsername(request.getUsername());
        }

        // ======================
        // EMAIL
        // ======================
        if (hasText(request.getEmail())
                && !request.getEmail().equals(user.getEmail())) {

            if (userRepository.existsByEmailAndIdNot(
                    request.getEmail(), userId)) {
                throw new DuplicateResourceException("Email already exists");
            }

            user.setEmail(request.getEmail());
        }

        // ======================
        // SIMPLE FIELDS
        // ======================
        setIfHasText(request.getName(), user::setName);
        setIfHasText(request.getMobile(), user::setMobile);
        setIfHasText(request.getPassword(), user::setPassword);
        setIfHasText(request.getEmergencyContactName(), user::setEmergencyContactName);
        setIfHasText(request.getEmergencyContactEmail(), user::setEmergencyContactEmail);
        setIfHasText(request.getEmergencyContactMobile(), user::setEmergencyContactMobile);

        // ======================
        // ENUM
        // ======================
        if (request.getProfileStatus() != null) {
            user.setProfileStatus(request.getProfileStatus());
        }

        userRepository.save(user);

        // ======================
        // Publish event ONLY if profile-related fields changed
        // ======================
        boolean profileChanged =
                !Objects.equals(oldName, user.getName())
                        || !Objects.equals(oldEmergencyContactName, user.getEmergencyContactName())
                        || !Objects.equals(oldEmergencyContactEmail, user.getEmergencyContactEmail())
                        || !Objects.equals(oldEmergencyContactMobile, user.getEmergencyContactMobile());

        // 🔔 Trigger email event
        if (profileChanged) {
            eventPublisher.publishEvent(
                    new UserProfileUpdatedEvent(
                            user.getEmail(),   // recipient
                            oldName, user.getName(),
                            oldEmergencyContactName, user.getEmergencyContactName(),
                            oldEmergencyContactEmail, user.getEmergencyContactEmail(),
                            oldEmergencyContactMobile, user.getEmergencyContactMobile()
                    )
            );
        }

        return user;
    }

    //helper method
    private void setIfHasText(String newValue, Consumer<String> stringConsumer) {
        if (StringUtils.hasText(newValue)) {
            stringConsumer.accept(newValue);
        }
    }



    @Override
    public Page<User> fetchAllUsers(Pageable usersListPageable) {
        return userRepository.findAll(usersListPageable);
    }

    @Override
    @Transactional
    public User deactivateUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setProfileStatus(ProfileStatus.INACTIVE);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User activateUser(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setProfileStatus(ProfileStatus.ACTIVE);

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User softdeleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        user.setProfileStatus(ProfileStatus.SOFT_DELETED);
        return user;
    }

    //do call this after soft delete so in log the status of SOFT_DELETED will be there
    @Override
    @Transactional
    public void deleteUser(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        userRepository.delete(user);
    }
}
