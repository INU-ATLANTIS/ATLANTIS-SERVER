package com.atl.map.common;

public interface ResponseMessage {

    // HTTP Status 200
    String SUCCESS = "Success.";

    // HTTP Status 400
    String VALIDATION_FAIL = "Validation failed.";
    String DUPLICATE_EMAIL = "Duplicate email.";
    String DUPLICATE_NICKNMAE = "Duplicate nickname.";
    String NOT_EXISTED_USER = "This user does not exist.";
    String NOT_EXISTED_POST = "This post does not exist.";

    // HTTP Status 401
    String SIGN_IN_FAIL = "Login information mismatch.";    
    String CERTIFICATION_FAIL = "Certification failed.";
    String MAIL_FAIL = "Mail send failed.";

    // HTTP Status 403
    String NO_PERMISSION = "Do not have Permission.";

    // HTTP Status 500
    String DATABASE_ERROR = "Database error.";
    
}
