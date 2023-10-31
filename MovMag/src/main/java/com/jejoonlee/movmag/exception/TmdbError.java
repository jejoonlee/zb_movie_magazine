package com.jejoonlee.movmag.exception;

public class TmdbError {

    public static ErrorCode check(Long code) {

        if (code == 1 || code == 12 || code == 13) return ErrorCode.TMDB_SUCCESS;
        else if (code == 2) return ErrorCode.TMDB_INVALID_SERVICE;
        else if (code == 3) return ErrorCode.TMDB_AUTHENTICATION_FAIL;
        else if (code == 4) return ErrorCode.TMDB_INVALID_FORMAT;
        else if (code == 5) return ErrorCode.TMDB_INVALID_PARAMETER;
        else if (code == 6) return ErrorCode.TMDB_INVALID_ID;
        else if (code == 7) return ErrorCode.TMDB_INVALID_API_KEY;
        else if (code == 8) return ErrorCode.TMDB_DUPLICATE_ENTRY;
        else if (code == 9) return ErrorCode.TMDB_SERVICE_OFFLINE;
        else if (code == 10) return ErrorCode.TMDB_SUSPENDED_API_KEY;
        else if (code == 11) return ErrorCode.TMDB_INTERNAL_ERROR;
        else if (code == 14) return ErrorCode.TMDB_AUTHENTICATION_FAILED;
        else if (code == 15) return ErrorCode.TMDB_FAILED;
        else if (code == 16) return ErrorCode.TMDB_DEVICE_DENIED;
        else if (code == 17) return ErrorCode.TMDB_SESSION_DENIED;
        else if (code == 18) return ErrorCode.TMDB_VALIDATION_FAILED;
        else if (code == 19) return ErrorCode.TMDB_INVALID_ACCEPT_HEADER;
        else if (code == 20) return ErrorCode.TMDB_INVALID_DATE_RANGE;
        else if (code == 21) return ErrorCode.TMDB_ENTRY_NOT_FOUND;
        else if (code == 22) return ErrorCode.TMDB_INVALID_PAGE;
        else if (code == 23) return ErrorCode.TMDB_INVALID_DATE;
        else if (code == 24) return ErrorCode.TMDB_SERVER_TIMEOUT;
        else if (code == 25) return ErrorCode.TMDB_REQUEST_OVER_LIMIT;
        else if (code == 26) return ErrorCode.TMDB_NO_USERNAME_PASSWORD;
        else if (code == 27) return ErrorCode.TMDB_TOO_MANY_RESPONSE;
        else if (code == 28) return ErrorCode.TMDB_INVALID_TIMEZONE;
        else if (code == 29) return ErrorCode.TMDB_CONFIRM_ACTION;
        else if (code == 30) return ErrorCode.TMDB_NOT_LOGGED_IN;
        else if (code == 31) return ErrorCode.TMDB_NOT_ACTIVE_USER;
        else if (code == 32) return ErrorCode.TMDB_EMAIL_NOT_VERIFIED;
        else if (code == 33) return ErrorCode.TMDB_INVALID_REQUEST_TOKEN;
        else if (code == 34) return ErrorCode.TMDB_RESOURCE_NOT_FOUND;
        else if (code == 35) return ErrorCode.TMDB_INVALID_TOKEN;
        else if (code == 36) return ErrorCode.TMDB_TOKEN_NOT_GRANTED_WRITE;
        else if (code == 37) return ErrorCode.TMDB_REQUESTED_SESSION_NOT_FOUND;
        else if (code == 38) return ErrorCode.TMDB_NO_PERMISSION_TO_EDIT;
        else if (code == 39) return ErrorCode.TMDB_PRIVATE_RESOURCE;
        else if (code == 40) return ErrorCode.TMDB_ALL_UPDATED;
        else if (code == 41) return ErrorCode.TMDB_NOT_APPROVED_TOKEN;
        else if (code == 42) return ErrorCode.TMDB_REQUEST_METHOD_NOT_SUPPORTED;
        else if (code == 43) return ErrorCode.TMDB_CAN_NOT_CONNECT_TO_SERVER;
        else if (code == 44) return ErrorCode.TMDB_ID_INVALID;
        else if (code == 45) return ErrorCode.TMDB_SUSPENDED_USER;
        else if (code == 46) return ErrorCode.TMDB_API_UNDER_MAINTENANCE;
        else if (code == 47) return ErrorCode.TMDB_INVALID_INPUT;


        return ErrorCode.TMDB_FAILED;
    }
}
