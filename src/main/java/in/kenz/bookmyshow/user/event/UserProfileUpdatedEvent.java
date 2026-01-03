package in.kenz.bookmyshow.user.event;

import in.kenz.bookmyshow.user.enums.ProfileStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserProfileUpdatedEvent {

    //Receiver's email ID
    private final String email;

    // User contact
    private final String oldName;
    private final String newName;

    // Emergency contact
    private final String oldEmergencyContactName;
    private final String newEmergencyContactName;

    private final String oldEmergencyContactEmail;
    private final String newEmergencyContactEmail;

    private final String oldEmergencyContactMobile;
    private final String newEmergencyContactMobile;



}