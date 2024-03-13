package com.atl.map.common;

public interface ResponseCode {
    
    // HTTP Status 200
    String SUCCESS = "SU";

    // HTTP Status 400
    String VALIDATION_FAIL = "VF";
    String DUPLICATE_EMAIL = "DE";
    String DUPLICATE_NICKNAME = "DN";
    String NOT_EXISTED_USER = "NU";
    String NOT_EXISTED_POST = "NP";

    // HTTP Status 401
    String SIGN_IN_FAIL = "SF";
    String CERTIFICATION_FAIL = "CF";
    String MAIL_FAIL = "MF";

    // HTTP Status 403
    String NO_PERMISSION = "NF";

    // HTTP Status 500
    String DATABASE_ERROR = "DBE";

}
