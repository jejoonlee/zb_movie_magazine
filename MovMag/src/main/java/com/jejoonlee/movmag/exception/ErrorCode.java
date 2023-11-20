package com.jejoonlee.movmag.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR("내부 서버 오류가 발생했습니다.", 5),
    INVALID_REQUEST("잘 못된 요청입니다", 4),
    NO_INPUT("빈칸 값이 있습니다", 4),
    JWT_SHOULD_NOT_BE_TRUSTED("JWT 토큰의 유효성이 확인이 안 됩니다. 다시 입력해주세요", 4),
    WRONG_INPUT_REQUEST("입력값을 제대로 입력하지 않았습니다. 다시 확인해주세요", 4),
    HTTP_REQUEST_BODY_ERROR("HTTP body에 문제가 생겼습니다. 다시 확인해주세요", 4),

    // 유저 관련 에러 코드
    USER_NOT_FOUND("없는 유저입니다", 4),
    EMAIL_EXISTS("이미 이메일이 존재합니다", 4),
    PASSWORD_NOT_MATCH("비밀번호가 일치하지 않습니다", 4),
    USER_PERMISSION_NOT_GRANTED("로그인이 필요한 서비스이거나, 해당 요청에 대한 권한이 없습니다. JWT 토큰을 헤더에 추가하거나, 다른 로그인 정보로 로그인해주세요", 6),
    USER_NOT_LOGGED_IN("로그인을 해주세요", 4),

    // 영화 업데이트 에러 코드
    MOVIE_NOT_FOUND("영화를 찾을 수 없습니다", 4),
    CAST_NOT_FOUND("캐스트를 찾을 수 없습니다", 4),
    PAGE_NOT_FOUND("찾는 페이지를 찾을 수 없습니다 (페이지는 0부터 시작합니다)", 4),
    WRONG_LANGUAGE("korean 또는 english으로만 영화를 검색할 수 있습니다", 4),
    NO_MOVIE_FOUND("영화를 찾을 수 없습니다", 4),

    TMDB_SUCCESS("TMDB 관련 요청이 정상적으로 작동 되었습니다", 2),
    TMDB_INVALID_SERVICE("해당 서비스는 유효하지 않습니다", 5),
    TMDB_AUTHENTICATION_FAIL("인증 실패 : 서비스를 이용할 권한이 없습니다",6),
    TMDB_INVALID_FORMAT("잘못된 형식입니다", 4),
    TMDB_INVALID_PARAMETER("요청 매개변수가 잘못되었습니다", 4),
    TMDB_INVALID_ID("ID가 잘못되었거나 찾을 수 없습니다", 4),
    TMDB_INVALID_API_KEY("API key가 유효하지 않습니다", 4),
    TMDB_DUPLICATE_ENTRY("제출하려는 데이터가 이미 존재합니다", 4),
    TMDB_SERVICE_OFFLINE("이 서비스는 일시적으로 오프라인 상태이므로 나중에 다시 시도하세요", 5),
    TMDB_SUSPENDED_API_KEY("중지된 API 키: 계정 액세스가 중지되었습니다. TMDB에 문의하세요", 4),
    TMDB_INTERNAL_ERROR("내부 오류: 문제가 발생했습니다. TMDB에 문의하세요", 5),
    TMDB_AUTHENTICATION_FAILED("인증 실패", 4),
    TMDB_FAILED("실패", 5),
    TMDB_DEVICE_DENIED("장치 거부", 4),
    TMDB_SESSION_DENIED("세션 거부", 4),
    TMDB_VALIDATION_FAILED("유효성 검사 실패", 4),
    TMDB_INVALID_ACCEPT_HEADER("헤더가 유효하지 않습니다", 4),
    TMDB_INVALID_DATE_RANGE("잘못된 날짜 범위: 14일을 초과하지 않는 범위여야 합니다", 4),
    TMDB_ENTRY_NOT_FOUND("항목을 찾을 수 없음: 편집하려는 항목을 찾을 수 없습니다", 4),
    TMDB_INVALID_PAGE("잘못된 페이지: 페이지는 1에서 시작하고 최대 500까지여야 합니다. 정수여야 합니다", 4),
    TMDB_INVALID_DATE("잘못된 날짜: 형식은 YYYY-MM-DD여야 합니다", 4),
    TMDB_SERVER_TIMEOUT("백엔드 서버로의 요청이 시간 초과되었습니다. 다시 시도하세요", 5),
    TMDB_REQUEST_OVER_LIMIT("요청 횟수가 허용된 제한인 (40)을 초과했습니다", 4),
    TMDB_NO_USERNAME_PASSWORD("사용자 이름과 암호를 제공해야 합니다", 4),
    TMDB_TOO_MANY_RESPONSE("객체가 너무 많습니다: 원격 호출의 최대 횟수는 20입니다", 4),
    TMDB_INVALID_TIMEZONE("잘못된 시간대: 유효한 시간대에 대한 설명서를 참조하세요", 4),
    TMDB_CONFIRM_ACTION(" 이 작업은 확인이 필요합니다: confirm=true 매개변수를 제공하세요", 4),
    TMDB_NOT_LOGGED_IN("잘못된 사용자 이름 및/또는 암호: 유효한 로그인 정보를 제공하지 않았습니다", 4),
    TMDB_NOT_ACTIVE_USER("계정이 더 이상 활성 상태가 아닙니다. 이것이 오류인 경우 TMDB에 문의하세요", 4),
    TMDB_EMAIL_NOT_VERIFIED("인증이 안 된 이메일 입니다", 4),
    TMDB_INVALID_REQUEST_TOKEN("잘못된 요청 토큰: 요청 토큰이 만료되었거나 잘못되었습니다", 4),
    TMDB_RESOURCE_NOT_FOUND("요청한 리소스를 찾을 수 없습니다", 4),
    TMDB_INVALID_TOKEN("잘못된 토큰", 4),
    TMDB_TOKEN_NOT_GRANTED_WRITE("사용자로부터 쓰기 권한이 부여되지 않은 이 토큰입니다", 4),
    TMDB_REQUESTED_SESSION_NOT_FOUND("요청한 세션을 찾을 수 없습니다", 4),
    TMDB_NO_PERMISSION_TO_EDIT("이 리소스를 편집할 권한이 없습니다", 6),
    TMDB_PRIVATE_RESOURCE("이 리소스는 비공개입니다", 4),
    TMDB_ALL_UPDATED("업데이트할 내용이 없습니다", 4),
    TMDB_NOT_APPROVED_TOKEN("이 요청 토큰은 사용자에 의해 승인되지 않았습니다", 6),
    TMDB_REQUEST_METHOD_NOT_SUPPORTED("이 리소스에 대해 지원되지 않는 요청 메서드입니다", 4),
    TMDB_CAN_NOT_CONNECT_TO_SERVER("백엔드 서버에 연결할 수 없습니다", 4),
    TMDB_ID_INVALID("ID가 잘못되었습니다", 4),
    TMDB_SUSPENDED_USER("이 사용자는 중지되었습니다", 6),
    TMDB_API_UNDER_MAINTENANCE("api가 보수 중에 있습니다", 4),
    TMDB_INVALID_INPUT("입력하신 값이 유효하지 않습니다", 4),

    // 리뷰 관련 에러
    REVIEW_NOT_FOUND("리뷰를 찾을 수 없습니다", 4),
    LOGGED_IN_USER_AND_AUTHOR_UNMATCH("리뷰를 쓴 사람과, 로그인한 유저가 다릅니다", 4),
    SAVE_TO_ELASTICSEARCH_UNSUCCESSFUL("엘라스틱서치에 저장하는 것을 실패했습니다", 5),
    ELASTICSEARCH_CAN_NOT_FIND_REVIEW("엘라스틱서치에서 리뷰를 찾을 수 없습니다", 4),

    COMMENT_NOT_FOUND("해당 댓글을 찾을 수 없습니다", 4),
    LOGGED_IN_USER_AND_COMMENT_USER_NOT_MATCH("댓글을 작성한 사람과 로그인한 사람이 일치하지 않습니다", 6);

    private String description;

    // 2 success
    // 4 client bad request
    // 5 internal server error
    // 6 Unauthorize
    private int errorNum;
}
