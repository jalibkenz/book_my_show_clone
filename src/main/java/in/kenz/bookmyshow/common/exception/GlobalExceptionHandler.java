package in.kenz.bookmyshow.common.exception;

import in.kenz.bookmyshow.common.dto.CommonResponse;
import in.kenz.bookmyshow.payment.exception.PaymentGatewayException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<CommonResponse<Void>> handleDuplicateResource(
            DuplicateResourceException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(CommonResponse.error(
                        ex.getMessage(),
                        "DUPLICATE_RESOURCE"
                ));
    }

    @ExceptionHandler(EmptyResultDataAccessException.class)
    public ResponseEntity<CommonResponse<Void>> handleEmptyResultDataAccessException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(CommonResponse.error(
                        ex.getMessage(),
                        "NOT_FOUND"
                ));
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CommonResponse<Void>> handleResourceNotFoundException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(CommonResponse.error(
                        ex.getMessage(),
                        "NOT_FOUND"
                ));
    }

    @ExceptionHandler(PaymentGatewayException.class)
    public ResponseEntity<CommonResponse<Void>> handlePaymentGatewayException(
            PaymentGatewayException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_GATEWAY)
                .body(CommonResponse.error(
                        ex.getMessage(),
                        "PAYMENT_GATEWAY_ERROR"
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<Void>> handleGenericException(
            Exception ex
    ) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.error(
                        "Something went wrong",
                        "INTERNAL_SERVER_ERROR"
                ));
    }


}