package com.jejoonlee.movmag.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieException extends RuntimeException{
    private ErrorCode errorCode;
    private String errorMessage;

    public MovieException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}