package in.kenz.bookmyshow.theatre.repository;

import in.kenz.bookmyshow.theatre.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TheatreRepository extends JpaRepository<Theatre, UUID> {
    boolean existsByName(String username);
    boolean existsByNameAndIdNot(String username, UUID userId);
}
