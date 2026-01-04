package in.kenz.bookmyshow.movie.service.impl;

import in.kenz.bookmyshow.movie.dto.CreateMovieRequest;
import in.kenz.bookmyshow.movie.entity.Movie;
import in.kenz.bookmyshow.movie.repository.MovieRepository;
import in.kenz.bookmyshow.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    @Override
    public Movie createMovie(CreateMovieRequest request) {
        Movie movie = Movie.builder()
                .title(request.getTitle())
                .durationMinutes(request.getDurationMinutes())
                .language(request.getLanguage())
                .genre(request.getGenre())
                .releaseDate(request.getReleaseDate())
                .rating(request.getRating())
                .build();

        return movieRepository.save(movie);
    }
}

