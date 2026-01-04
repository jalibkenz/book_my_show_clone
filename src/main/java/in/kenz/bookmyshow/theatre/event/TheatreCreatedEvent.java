package in.kenz.bookmyshow.theatre.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TheatreCreatedEvent {
    private final String name;

    private final String city;
    private final String state;
    private final String country;

    private final String email;
    private final String mobile;
}