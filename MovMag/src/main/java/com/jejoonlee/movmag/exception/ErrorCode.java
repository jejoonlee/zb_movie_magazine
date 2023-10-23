package com.jejoonlee.movmag.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다."),
    INVALID_REQUEST("잘 못된 요청입니다"),
    NO_INPUT("빈칸 값이 있습니다"),
    JWT_SHOULD_NOT_BE_TRUSTED("JWT 토큰의 유효성이 확인이 안 됩니다. 다시 입력해주세요"),
    WRONG_INPUT_REQUEST("입력값을 제대로 입력하지 않았습니다. 다시 확인해주세요"),

    // 유저 관련 에러 코드
    USER_NOT_FOUND("없는 유저입니다"),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다"),
    USER_PERMISSION_NOT_GRANTED("해당 요청에 대한 권한이 없습니다. JWT 토큰을 헤더에 추가하거나, 다른 로그인 정보로 로그인해주세요");

    private String description;
}
