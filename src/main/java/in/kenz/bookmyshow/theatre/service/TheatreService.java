package in.kenz.bookmyshow.theatre.service;

import in.kenz.bookmyshow.theatre.dto.SignupTheatreRequest;
import in.kenz.bookmyshow.theatre.dto.UpdateTheatreRequest;
import in.kenz.bookmyshow.theatre.entity.Theatre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public interface TheatreService {
    Theatre createTheatre(SignupTheatreRequest signupTheatreRequest);
    Theatre fetchTheatre(UUID theatreId);
    Theatre updateTheatre(UUID theatreId, UpdateTheatreRequest request);
    Page<Theatre> fetchAllTheatres(Pageable theatreListPageable);
    Theatre deactivateTheatre(UUID theatreId);
    Theatre activateTheatre(UUID theatreId);
    Theatre softdeleteTheatre(UUID theatreId);
    void deleteTheatre(UUID theatreId);
}
