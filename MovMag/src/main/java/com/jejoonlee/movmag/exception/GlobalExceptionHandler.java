package com.jejoonlee.movmag.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.SignatureException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static HttpStatus getHttpStatus(int num){
        if (num == 4) {
            return HttpStatus.BAD_REQUEST;
        } else if (num == 5) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.UNAUTHORIZED; // 6일 때
        }
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> handleMemberException(MemberException e) {
        log.error("{} has occurred.", e.getErrorCode());

        return ResponseEntity.status(getHttpStatus(e.getErrorNum())).body(
                ErrorResponse.getErrorCode(e.getErrorCode()));
    }

    @ExceptionHandler(MovieException.class)
    public ResponseEntity<ErrorResponse> handleMovieException(MovieException e) {
        log.error("{} has occurred.", e.getErrorCode());

        return ResponseEntity.status(getHttpStatus(e.getErrorNum())).body(
                ErrorResponse.getErrorCode(e.getErrorCode()));
    }

    @ExceptionHandler(ReviewClientException.class)
    public ResponseEntity<ErrorResponse> handleReviewException(ReviewClientException e) {
        log.error("{} has occurred.", e.getErrorCode());

        return ResponseEntity.status(getHttpStatus(e.getErrorNum())).body(
                ErrorResponse.getErrorCode(e.getErrorCode()));
    }

    @ExceptionHandler(CommentException.class)
    public ResponseEntity<ErrorResponse> handleCommentException(CommentException e) {
        log.error("{} has occurred.", e.getErrorCode());

        return ResponseEntity.status(getHttpStatus(e.getErrorNum())).body(
                ErrorResponse.getErrorCode(e.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("MethodArgumentNotValidException : " + e.getMessage());

        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException has occurred : " + e.getMessage(), e);

        return ResponseEntity.badRequest().body(
                ErrorResponse.getErrorCode(ErrorCode.INVALID_REQUEST));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException e) {
        log.error("AccessDeniedException has occurred: " + e.getMessage(), e);

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                ErrorResponse.getErrorCode(ErrorCode.USER_PERMISSION_NOT_GRANTED));
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(NullPointerException e) {
        log.error("NullPointerException has occurred : " + e.getMessage());

        return ResponseEntity.badRequest().body(
                ErrorResponse.getErrorCode(ErrorCode.NO_INPUT));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleSignatureException(SignatureException e) {
        log.error("SignatureException has occured : " + e.getMessage());

        return ResponseEntity.badRequest().body(
                ErrorResponse.getErrorCode(ErrorCode.JWT_SHOULD_NOT_BE_TRUSTED));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleJsonParseException(HttpMessageNotReadableException e) {
        log.error("HttpMessageNotReadableException has occured : " + e.getMessage());

        return ResponseEntity.badRequest().body(
                ErrorResponse.getErrorCode(ErrorCode.HTTP_REQUEST_BODY_ERROR));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception has occurred: " + e.getMessage(), e);

        return ResponseEntity.internalServerError().body(
                ErrorResponse.getErrorCode(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
