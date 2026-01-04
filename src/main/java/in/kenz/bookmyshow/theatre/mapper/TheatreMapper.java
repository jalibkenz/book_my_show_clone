package in.kenz.bookmyshow.theatre.mapper;

import in.kenz.bookmyshow.theatre.dto.SignupTheatreResponse;
import in.kenz.bookmyshow.theatre.dto.UpdateTheatreResponse;
import in.kenz.bookmyshow.theatre.entity.Theatre;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TheatreMapper {
    SignupTheatreResponse toSignupTheatreResponse(Theatre theatre);
    UpdateTheatreResponse toUpdateTheatreResponse(Theatre theatre);

}
