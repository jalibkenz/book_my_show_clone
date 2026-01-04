package in.kenz.bookmyshow.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserRequest {
    private String name;
    private String username;
    private String password;
    private String email;

}
