package org.example.springlogbackend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final String status;
    private final String code;
    private final String message;
    private List<FieldError> errors;

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                "error",
                errorCode.name(),
                errorCode.getMessage(),
                null
        );
    }

    public static ErrorResponse of(ErrorCode errorCode, List<FieldError> errors) {
        return new ErrorResponse(
                "error",
                errorCode.name(),
                errorCode.getMessage(),
                errors
        );
    }

    @AllArgsConstructor
    public static class FieldError {
        private final String field;
        private final String message;

        public static List<FieldError> of(BindingResult bindingResult) {
            return bindingResult.getFieldErrors().stream()
                    .map(error -> new FieldError(error.getField(), error.getDefaultMessage()))
                    .toList();
        }
    }
}
