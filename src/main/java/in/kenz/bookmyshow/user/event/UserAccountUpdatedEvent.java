package in.kenz.bookmyshow.user.event;

import in.kenz.bookmyshow.user.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAccountUpdatedEvent {
    

    private final String oldUsername;
    private final String newUsername;

    private final String oldEmail;
    private final String newEmail;

    private final String oldMobile;
    private final String newMobile;

    private final ProfileStatus oldProfileStatus;
    private final ProfileStatus newProfileStatus;


}