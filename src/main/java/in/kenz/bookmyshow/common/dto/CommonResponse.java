package in.kenz.bookmyshow.common.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {
    //1
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MMM-dd hh:mm ss a")
    private LocalDateTime timestamp;

    //2
    private boolean success;

    //3
    private String message;

    //4
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String errorCode;

    //5
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;




    // Static helper for successful responses
    public static <T> CommonResponse<T> success(T data) {
        return CommonResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(true)
                .message("Operation successful")
                .data(data)
                .build();
    }

    // Static helper for error responses
    public static <T> CommonResponse<T> error(String message, String errorCode) {
        return CommonResponse.<T>builder()
                .timestamp(LocalDateTime.now())
                .success(false)
                .message(message)
                .errorCode(errorCode)
                .build();
    }
}