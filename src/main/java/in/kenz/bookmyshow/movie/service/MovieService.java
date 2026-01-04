package in.kenz.bookmyshow.movie.service;

import in.kenz.bookmyshow.movie.dto.CreateMovieRequest;
import in.kenz.bookmyshow.movie.entity.Movie;

public interface MovieService {
    Movie createMovie(CreateMovieRequest request);
}

