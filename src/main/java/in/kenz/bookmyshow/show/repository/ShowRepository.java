package in.kenz.bookmyshow.show.repository;

import in.kenz.bookmyshow.show.entity.Show;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ShowRepository extends JpaRepository<Show, UUID> {

    @Query("""
        select count(s) > 0
        from Show s
        where s.theatreId = :theatreId
          and s.screenName = :screenName
          and s.startTime < :newEnd
          and s.endTime > :newStart
    """)
    boolean existsOverlappingShowOnScreen(
            UUID theatreId,
            String screenName,
            LocalDateTime newStart,
            LocalDateTime newEnd
    );

    List<Show> findByTheatreId(UUID theatreId);
}