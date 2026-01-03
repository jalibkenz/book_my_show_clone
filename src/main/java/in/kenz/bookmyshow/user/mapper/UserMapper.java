package in.kenz.bookmyshow.user.mapper;

import in.kenz.bookmyshow.user.dto.UpdateResponse;
import in.kenz.bookmyshow.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UpdateResponse toUpdateResponse(User user);
}
