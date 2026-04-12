package com.atl.map.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 400 Bad Request
    VALIDATION_FAIL(HttpStatus.BAD_REQUEST, "VF", "Validation failed."),
    DUPLICATE_EMAIL(HttpStatus.BAD_REQUEST, "DE", "Duplicate email."),
    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST, "DN", "Duplicate nickname."),
    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "NU", "This user does not exist."),
    POST_NOT_FOUND(HttpStatus.BAD_REQUEST, "NP", "This post does not exist."),
    MARKER_NOT_FOUND(HttpStatus.BAD_REQUEST, "NM", "This marker does not exist."),
    BUILDING_NOT_FOUND(HttpStatus.BAD_REQUEST, "NB", "This building does not exist."),
    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, "NC", "This comment does not exist."),
    NOTIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, "NN", "This notification does not exist."),
    INVALID_FILE(HttpStatus.BAD_REQUEST, "IF", "Invalid file request."),

    // 401 Unauthorized
    AUTHENTICATION_REQUIRED(HttpStatus.UNAUTHORIZED, "AR", "Authentication is required."),
    SIGN_IN_FAIL(HttpStatus.UNAUTHORIZED, "SF", "Login information mismatch."),
    CERTIFICATION_FAIL(HttpStatus.UNAUTHORIZED, "CF", "Certification failed."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "IRT", "Invalid refresh token."),

    // 403 Forbidden
    NO_PERMISSION(HttpStatus.FORBIDDEN, "NF", "Do not have Permission."),
    REPORTED_USER(HttpStatus.FORBIDDEN, "RU.", "This user is reported."),

    // 404 Not Found
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "FNF", "This file does not exist."),

    // 429 Too Many Requests
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "TMR", "Too many requests."),

    // 500 Internal Server Error
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DBE", "Database error."),
    MAIL_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "MF", "Mail send failed."),
    FILE_UPLOAD_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "FUF", "File upload failed.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
