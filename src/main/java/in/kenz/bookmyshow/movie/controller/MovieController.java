package in.kenz.bookmyshow.movie.controller;

import in.kenz.bookmyshow.movie.dto.CreateMovieRequest;
import in.kenz.bookmyshow.movie.entity.Movie;
import in.kenz.bookmyshow.movie.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @PostMapping
    @Operation(summary = "createMovie")
    public ResponseEntity<Movie> createMovie(@RequestBody @Valid CreateMovieRequest request) {
        Movie saved = movieService.createMovie(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}

