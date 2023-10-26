package com.jejoonlee.movmag.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;

    public static ErrorResponse getErrorCode(ErrorCode code) {
        return ErrorResponse.builder()
                .errorCode(code)
                .errorMessage(code.getDescription())
                .build();
    }
}
