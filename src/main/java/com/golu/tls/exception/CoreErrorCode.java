package com.golu.tls.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CoreErrorCode implements ErrorCode {
    SUCCESS(200, false),
    INTERNAL_SERVER_ERROR(500, false),
    JSON_ERROR(500, false),
    ;

    private int httpStatus;
    private boolean stackTraceLoggingEnabled;

}
