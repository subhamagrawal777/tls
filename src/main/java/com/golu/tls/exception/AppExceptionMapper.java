package com.golu.tls.exception;

import com.golu.tls.models.AppResponse;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.Map;

@Slf4j
public class AppExceptionMapper implements ExceptionMapper<AppException> {

    @Override
    public Response toResponse(AppException exception) {
        if (exception.getErrorCode().isStackTraceLoggingEnabled()) {
            log.error("ERROR Message: {}, context: {}", exception.getMessage(), exception.getContext(), exception);
        } else {
            log.error("ERROR Message: {}, context: {}", exception.getMessage(), exception.getContext());
        }
        return Response.status(exception.getErrorCode().getHttpStatus())
                .entity(AppResponse.<Map<String, Object>>builder()
                        .success(false)
                        .errorCode(exception.getErrorCode().name())
                        .data(exception.getContext())
                        .build())
                .build();
    }
}

