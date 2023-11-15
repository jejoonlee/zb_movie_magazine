package com.jejoonlee.movmag.exception;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberException extends RuntimeException{
    private ErrorCode errorCode;
    private String errorMessage;
    private int errorNum;

    public MemberException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
        this.errorNum = errorCode.getErrorNum();
    }
}
