package in.kenz.bookmyshow.theatre.dto;

import in.kenz.bookmyshow.theatre.enums.ProfileStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateTheatreRequest {

    private String name;
    private String Address;

    private String city;
    private String state;
    private String country;

    private String email;
    private String mobile;

    private ProfileStatus profileStatus;

    private String emergencyContactName;
    private String emergencyContactEmail;
    private String emergencyContactMobile;

}
