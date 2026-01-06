package in.kenz.bookmyshow.screenandseat.mapper;

import in.kenz.bookmyshow.screenandseat.dto.SeatDto;
import in.kenz.bookmyshow.screenandseat.entity.Seat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    SeatDto toDto(Seat seat);
}

