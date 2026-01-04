package in.kenz.bookmyshow.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class CreateUserResponse {
    private UUID id;
    private String name;
    private String username;
    private String email;
}
