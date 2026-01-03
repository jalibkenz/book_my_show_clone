package in.kenz.bookmyshow.user.service;

import in.kenz.bookmyshow.user.dto.SignupRequest;
import in.kenz.bookmyshow.user.dto.UpdateRequest;
import in.kenz.bookmyshow.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface UserService {
    User createUser(SignupRequest signupRequest);
    User fetchUser(UUID userId);
    User updateUser(UUID userId, UpdateRequest request);
    Page<User> fetchAllUsers(Pageable usersListPageable);
    User deactivateUser(UUID userId);
    User activateUser(UUID userId);
    User softdeleteUser(UUID userId);
    void deleteUser(UUID userId);
}
