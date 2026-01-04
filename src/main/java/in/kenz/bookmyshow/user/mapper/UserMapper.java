package in.kenz.bookmyshow.user.mapper;

import in.kenz.bookmyshow.user.dto.CreateUserResponse;
import in.kenz.bookmyshow.user.dto.UpdateUserResponse;
import in.kenz.bookmyshow.user.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UpdateUserResponse toUpdateResponse(User user);
    CreateUserResponse toSignupResponse(User user);
}
