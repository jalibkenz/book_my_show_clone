package in.kenz.bookmyshow.user.controller;

import in.kenz.bookmyshow.common.dto.CommonResponse;
import in.kenz.bookmyshow.user.dto.CreateUserRequest;
import in.kenz.bookmyshow.user.dto.CreateUserResponse;
import in.kenz.bookmyshow.user.dto.UpdateUserRequest;
import in.kenz.bookmyshow.user.dto.UpdateUserResponse;
import in.kenz.bookmyshow.user.entity.User;
import in.kenz.bookmyshow.user.mapper.UserMapper;
import in.kenz.bookmyshow.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@Tag(name = "User Module")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/me/create")
    @Operation(summary = "createUser")
    public ResponseEntity<CommonResponse<CreateUserResponse>> createUser(@RequestBody CreateUserRequest createUserRequest) {

        User user = userService.createUser(createUserRequest);

        CreateUserResponse createUserResponse = userMapper.toSignupResponse(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(createUserResponse));
    }

    @GetMapping("/me/{userId}")
    @Operation(summary = "fetchUser")
    public ResponseEntity<CommonResponse<UpdateUserResponse>> fetchUser(@PathVariable UUID userId) {

        User user = userService.fetchUser(userId);
        UpdateUserResponse updateUserResponse = userMapper.toUpdateResponse(user);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(updateUserResponse));
    }




    @PatchMapping("/me/{userId}")
    @Operation(summary = "updateUser")
    public ResponseEntity<CommonResponse<UpdateUserResponse>> updateUser(@PathVariable UUID userId, @RequestBody UpdateUserRequest updateUserRequest) {

        // Here you would typically have logic to handle the session or authorization, but for now we use the path variable.
        User user = userService.updateUser(userId, updateUserRequest);



        UpdateUserResponse updateUserResponse = UpdateUserResponse.builder()
                .name(user.getName())
                .username(user.getUsername())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .profileStatus(user.getProfileStatus())
                .emergencyContactName(user.getEmergencyContactName())
                .emergencyContactEmail(user.getEmergencyContactEmail())
                .emergencyContactMobile(user.getEmergencyContactMobile())
                .build();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(updateUserResponse));
    }


    @GetMapping("/list")
    @Operation(summary = "fetchAllUsers")
    @Tag(name = "AppAdmin Module")
    public ResponseEntity<CommonResponse<Page<User>>> fetchAllUsers(@ParameterObject @PageableDefault(size = 1, sort = "username") Pageable pageable) {

        Page<User> userPage = userService.fetchAllUsers(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(userPage));
    }

    @PatchMapping("/me/deactivate/{userId}")
    @Operation(summary = "deactivateUser")
    @Tag(name = "AppAdmin Module")
    public ResponseEntity<CommonResponse<User>> deactivateUser(@PathVariable UUID userId) {

        User user = userService.deactivateUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(user));
    }

    @PatchMapping("/me/activate/{userId}")
    @Operation(summary = "activateUser")
    @Tag(name = "AppAdmin Module")
    public ResponseEntity<CommonResponse<User>> activateUser(@PathVariable UUID userId) {

        User user = userService.activateUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(user));
    }





    @DeleteMapping("/me/softdelete/{userId}")
    @Operation(summary = "softdeleteUser")
    @Tag(name = "AppAdmin Module")
    public ResponseEntity<CommonResponse<User>> softdeleteUser(@PathVariable UUID userId) {
        User user = userService.softdeleteUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(user));
    }

    @DeleteMapping("/me/delete/{userId}")
    @Operation(summary = "deleteUser")
    @Tag(name = "AppAdmin Module")
    public ResponseEntity<CommonResponse<Void>> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


}
