package in.kenz.bookmyshow.theatre.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


@Getter
@Setter
public class SignupTheatreResponse {
    private UUID id;
    private String name;

    private String city;
    private String state;
    private String country;

    private String email;
    private String mobile;

}
