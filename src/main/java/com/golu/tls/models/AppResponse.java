package com.golu.tls.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppResponse<T> {
    private boolean success;

    private String errorCode;

    private String responseMessage;

    private T data;
}
