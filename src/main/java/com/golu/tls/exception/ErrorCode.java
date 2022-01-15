package com.golu.tls.exception;

public interface ErrorCode {
    int getHttpStatus();

    boolean isStackTraceLoggingEnabled();

    String name();
}
