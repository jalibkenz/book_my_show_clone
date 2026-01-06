package in.kenz.bookmyshow.screenandseat.controller;

import in.kenz.bookmyshow.screenandseat.dto.CreateShowRequest;
import in.kenz.bookmyshow.screenandseat.entity.Show;
import in.kenz.bookmyshow.screenandseat.service.ShowService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
@Tag(name = "ScreenAndSeat Module")
public class ShowController {

    private final ShowService showService;

    @PostMapping
    @Operation(summary = "createShow")
    public ResponseEntity<Show> createShow(
            @RequestBody @Valid CreateShowRequest request
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(showService.createShow(request));
    }
}