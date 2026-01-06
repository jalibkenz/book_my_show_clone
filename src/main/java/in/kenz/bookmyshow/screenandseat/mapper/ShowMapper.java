package in.kenz.bookmyshow.screenandseat.mapper;

import in.kenz.bookmyshow.screenandseat.dto.ShowDto;
import in.kenz.bookmyshow.screenandseat.entity.Show;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShowMapper {
    ShowDto toDto(Show show);
}

