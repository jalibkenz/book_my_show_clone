package in.kenz.bookmyshow.theatre.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupTheatreRequest {
    private String name;

    private String city;
    private String state;
    private String country;

    private String email;
    private String mobile;

}

