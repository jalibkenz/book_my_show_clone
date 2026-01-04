package in.kenz.bookmyshow.movie.repository;

import in.kenz.bookmyshow.movie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
}

