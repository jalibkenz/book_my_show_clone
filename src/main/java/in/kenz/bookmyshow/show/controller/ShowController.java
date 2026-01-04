package in.kenz.bookmyshow.show.controller;

import in.kenz.bookmyshow.show.dto.CreateShowRequest;
import in.kenz.bookmyshow.show.entity.Show;
import in.kenz.bookmyshow.show.service.ShowService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/shows")
@RequiredArgsConstructor
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