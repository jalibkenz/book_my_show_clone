package in.kenz.bookmyshow.theatre.controller;

import in.kenz.bookmyshow.common.dto.CommonResponse;
import in.kenz.bookmyshow.theatre.dto.SignupTheatreRequest;
import in.kenz.bookmyshow.theatre.dto.SignupTheatreResponse;
import in.kenz.bookmyshow.theatre.dto.UpdateTheatreRequest;
import in.kenz.bookmyshow.theatre.dto.UpdateTheatreResponse;
import in.kenz.bookmyshow.theatre.entity.Theatre;
import in.kenz.bookmyshow.theatre.mapper.TheatreMapper;
import in.kenz.bookmyshow.theatre.service.TheatreService;
import in.kenz.bookmyshow.user.dto.SignupRequest;
import in.kenz.bookmyshow.user.dto.SignupResponse;
import in.kenz.bookmyshow.user.dto.UpdateRequest;
import in.kenz.bookmyshow.user.dto.UpdateResponse;
import in.kenz.bookmyshow.user.entity.User;
import in.kenz.bookmyshow.user.mapper.UserMapper;
import in.kenz.bookmyshow.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/theatre")
public class TheatreController {

    private final TheatreService theatreService;
    private final TheatreMapper theatreMapper;

    public TheatreController(TheatreService theatreService, TheatreMapper theatreMapper) {
        this.theatreService = theatreService;
        this.theatreMapper = theatreMapper;
    }

    @PostMapping("/create")
    @Operation(summary = "createTheatre")
    public ResponseEntity<CommonResponse<SignupTheatreResponse>> createTheatre(@RequestBody SignupTheatreRequest signupTheatreRequest) {


        Theatre theatre = theatreService.createTheatre(signupTheatreRequest);
        log.info("Theatre ID after save: {}", theatre.getId());
        SignupTheatreResponse signupTheatreResponse = theatreMapper.toSignupTheatreResponse(theatre);
        log.info("MAPPER Theatre ID after save: {}", signupTheatreResponse.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(CommonResponse.success(signupTheatreResponse));
    }

    @GetMapping("/{theatreId}")
    @Operation(summary = "fetchTheatre")
    public ResponseEntity<CommonResponse<UpdateTheatreResponse>> fetchTheatre(@PathVariable UUID theatreId) {

        Theatre theatre = theatreService.fetchTheatre(theatreId);
        UpdateTheatreResponse updateTheatreResponse = theatreMapper.toUpdateTheatreResponse(theatre);


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(updateTheatreResponse));
    }




    @PatchMapping("/{theatreId}")
    @Operation(summary = "updateTheatre")
    public ResponseEntity<CommonResponse<UpdateTheatreResponse>> updateTheatre(
            @PathVariable UUID theatreId, @RequestBody UpdateTheatreRequest updateTheatreRequest) {

        // Here you would typically have logic to handle the session or authorization, but for now we use the path variable.
        Theatre theatre = theatreService.updateTheatre(theatreId, updateTheatreRequest);

        UpdateTheatreResponse updateTheatreResponse = theatreMapper.toUpdateTheatreResponse(theatre);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(updateTheatreResponse));
    }





    @GetMapping("/list")
    @Operation(summary = "fetchAllTheatres")
    public ResponseEntity<CommonResponse<Page<Theatre>>> fetchAllTheatres(@ParameterObject @PageableDefault(size = 1, sort = "name") Pageable pageable) {

        Page<Theatre> theatrePage = theatreService.fetchAllTheatres(pageable);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(theatrePage));
    }





    @PatchMapping("/deactivate/{theatreId}")
    @Operation(summary = "deactivateTheatre")
    public ResponseEntity<CommonResponse<Theatre>> deactivateTheatre(@PathVariable UUID theatreId) {

        Theatre theatre = theatreService.deactivateTheatre(theatreId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(theatre));
    }





    @PatchMapping("/activate/{theatreId}")
    @Operation(summary = "activateTheatre")
    public ResponseEntity<CommonResponse<Theatre>> activateTheatre(@PathVariable UUID theatreId) {

        Theatre theatre  = theatreService.activateTheatre(theatreId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(theatre));
    }





    @DeleteMapping("/me/softdelete/{theatreId}")
    @Operation(summary = "softdeleteTheatre")
    public ResponseEntity<CommonResponse<Theatre>> softdeleteTheatre(@PathVariable UUID theatreId) {
        Theatre theatre = theatreService.softdeleteTheatre(theatreId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(CommonResponse.success(theatre));
    }

    @DeleteMapping("/delete/{theatreId}")
    @Operation(summary = "deleteTheatre")
    public ResponseEntity<CommonResponse<Void>> deleteTheatre(@PathVariable UUID theatreId) {
        theatreService.deleteTheatre(theatreId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .build();
    }


}
