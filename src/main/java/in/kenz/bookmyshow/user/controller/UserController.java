package in.kenz.bookmyshow.user.controller;

import in.kenz.bookmyshow.common.dto.CommonResponse;
import in.kenz.bookmyshow.theatre.entity.Theatre;
import in.kenz.bookmyshow.user.dto.SignupRequest;
import in.kenz.bookmyshow.user.dto.SignupResponse;
import in.kenz.bookmyshow.user.dto.UpdateRequest;
import in.kenz.bookmyshow.user.dto.UpdateResponse;
import in.kenz.bookmyshow.user.entity.User;
import in.kenz.bookmyshow.user.mapper.UserMapper;
import in.kenz.bookmyshow.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping("/me/create")
    @Operation(summary = "createUser")
    public ResponseEntity<CommonResponse<SignupResponse>> createUser(@RequestBody SignupRequest signupRequest) {

        User user = userService.createUser(signupRequest);

        SignupResponse signupResponse = userMapper.toSignupResponse(user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(signupResponse));
    }

    @GetMapping("/me/{userId}")
    @Operation(summary = "fetchUser")
    public ResponseEntity<CommonResponse<UpdateResponse>> fetchUser(@PathVariable UUID userId) {

        User user = userService.fetchUser(userId);
        UpdateResponse updateResponse = userMapper.toUpdateResponse(user);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(updateResponse));
    }




    @PatchMapping("/me/{userId}")
    @Operation(summary = "updateUser")
    public ResponseEntity<CommonResponse<UpdateResponse>> updateUser(@PathVariable UUID userId, @RequestBody UpdateRequest updateRequest) {

        // Here you would typically have logic to handle the session or authorization, but for now we use the path variable.
        User user = userService.updateUser(userId, updateRequest);



        UpdateResponse updateResponse = UpdateResponse.builder()
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
                .body(CommonResponse.success(updateResponse));
    }


    @GetMapping("/list")
    @Operation(summary = "fetchAllUsers")
    public ResponseEntity<CommonResponse<Page<User>>> fetchAllUsers(@ParameterObject @PageableDefault(size = 1, sort = "username") Pageable pageable) {

        Page<User> userPage = userService.fetchAllUsers(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(userPage));
    }

    @PatchMapping("/me/deactivate/{userId}")
    @Operation(summary = "deactivateUser")
    public ResponseEntity<CommonResponse<User>> deactivateUser(@PathVariable UUID userId) {

        User user = userService.deactivateUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(user));
    }

    @PatchMapping("/me/activate/{userId}")
    @Operation(summary = "activateUser")
    public ResponseEntity<CommonResponse<User>> activateUser(@PathVariable UUID userId) {

        User user = userService.activateUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(user));
    }





    @DeleteMapping("/me/softdelete/{userId}")
    @Operation(summary = "softdeleteUser")
    public ResponseEntity<CommonResponse<User>> softdeleteUser(@PathVariable UUID userId) {
        User user = userService.softdeleteUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(user));
    }

    @DeleteMapping("/me/delete/{userId}")
    @Operation(summary = "deleteUser")
    public ResponseEntity<CommonResponse<Void>> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


}
