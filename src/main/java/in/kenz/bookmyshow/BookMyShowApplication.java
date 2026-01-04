package in.kenz.bookmyshow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.cache.annotation.EnableCaching;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;

@SpringBootApplication
@EnableAsync
@EnableCaching
@OpenAPIDefinition(
        info = @Info(title = "BookMyShow API", version = "1.0"),
        tags = {
                @Tag(name = "User Module", description = "Operations related to general users"),
                @Tag(name = "Payment Module", description = "Payment configurations and general payment operations"),
                @Tag(name = "Donation Module", description = "Operations for donations"),
                @Tag(name = "AppAdmin Module", description = "Super Admin platform-level operations"),
                @Tag(name = "Theatre Module", description = "Theatre management"),
                @Tag(name = "ScreenAndSeat Module", description = "Screen and seat management, including shows and seats"),
                @Tag(name = "Movie Module", description = "Movie management"),
                @Tag(name = "Booking Module", description = "Booking journey, payments for bookings, and ticket management")
        }
)
public class BookMyShowApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookMyShowApplication.class, args);
	}

}
