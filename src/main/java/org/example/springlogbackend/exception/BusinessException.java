package org.example.springlogbackend.exception;

import lombok.Getter;
import org.example.springlogbackend.dto.ErrorCode;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String logMessage) {
        super(logMessage);
        this.errorCode = errorCode;
    }
}
