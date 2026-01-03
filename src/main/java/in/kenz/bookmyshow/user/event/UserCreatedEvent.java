package in.kenz.bookmyshow.user.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreatedEvent {
    private final String email;
    private final String name;
}