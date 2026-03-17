package mn.landing.landing_service.Common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApiExceptionHandler {

    public record ApiError(String code) {}

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntime(RuntimeException ex) {
        String code = (ex.getMessage() == null || ex.getMessage().isBlank())
                ? "ERROR"
                : ex.getMessage();

        HttpStatus status = switch (code) {
            case "UNAUTHORIZED" -> HttpStatus.UNAUTHORIZED;
            case "FORBIDDEN" -> HttpStatus.FORBIDDEN;

            case "LANDING_NOT_FOUND", "TEMPLATE_NOT_FOUND", "USER_NOT_FOUND", "PLAN_NOT_FOUND", "PAYMENT_NOT_FOUND"
                    -> HttpStatus.NOT_FOUND;

            default -> HttpStatus.BAD_REQUEST;
        };

        return ResponseEntity.status(status).body(new ApiError(code));
    }
}
