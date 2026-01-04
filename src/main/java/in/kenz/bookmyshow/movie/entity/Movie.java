package in.kenz.bookmyshow.movie.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "movies")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    @Column(length = 50)
    private String language;

    @Column(length = 100)
    private String genre;

    private LocalDate releaseDate;

    @Column(precision = 3, scale = 1)
    private BigDecimal rating; // e.g., 8.5
}

