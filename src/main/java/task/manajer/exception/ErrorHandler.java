package task.manajer.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handle404Error() {
        return ResponseEntity.notFound()
                .build();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handle405Error() {
        return ResponseEntity.status(405)
                .body(new ErrorResponse(405, "Method not allowed"));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ValidationErrorData>> handleValidationError(MethodArgumentNotValidException ex) {
        var errors = ex.getFieldErrors().stream()
                .map(ValidationErrorData::new)
                .toList();

        return ResponseEntity.badRequest()
                .body(errors);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleInternalError(Exception ex) {
        return ResponseEntity.internalServerError()
                .body(new ErrorResponse(500, "Internal server error"));
    }

    private record ValidationErrorData(
            String field,
            String message
    ) {
        public ValidationErrorData(FieldError error) {
            this(error.getField(), error.getDefaultMessage());
        }
    }

    private record ErrorResponse(
            int status,
            String message
    ) {}
}