package in.kenz.bookmyshow.user.dto;

import in.kenz.bookmyshow.user.enums.ProfileStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateRequest {
    private String name;
    private String username;
    private String password;
    private String email;
    private String mobile;

    private ProfileStatus profileStatus;

    private String emergencyContactName;
    private String emergencyContactEmail;
    private String emergencyContactMobile;
}
