package in.kenz.bookmyshow.theatre.event;

import in.kenz.bookmyshow.theatre.enums.ProfileStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class TheatreProfileUpdatedEvent {

    // Basic profile
    private final String oldName;
    private final String newName;

    private final String oldAddress;
    private final String newAddress;


    //location
    private final String oldCity;
    private final String newCity;

    private final String oldState;
    private final String newState;

    private final String oldCountry;
    private final String newCountry;


    // Contact details
    private final String oldEmail;
    private final String newEmail;

    private final String oldMobile;
    private final String newMobile;

    // Profile status
    private final ProfileStatus oldProfileStatus;
    private final ProfileStatus newProfileStatus;

    // Emergency contact
    private final String oldEmergencyContactName;
    private final String newEmergencyContactName;

    private final String oldEmergencyContactEmail;
    private final String newEmergencyContactEmail;

    private final String oldEmergencyContactMobile;
    private final String newEmergencyContactMobile;
}