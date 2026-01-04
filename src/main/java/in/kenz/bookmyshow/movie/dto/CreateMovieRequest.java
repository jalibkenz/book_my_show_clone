package in.kenz.bookmyshow.movie.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CreateMovieRequest {

    @NotBlank
    private String title;

    @NotNull
    @Min(1)
    private Integer durationMinutes;

    private String language;

    private String genre;

    private LocalDate releaseDate;

    private BigDecimal rating;
}

