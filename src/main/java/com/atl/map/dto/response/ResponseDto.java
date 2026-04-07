package com.atl.map.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter // Lombok 라이브러리를 사용하여 모든 필드의 getter 메서드를 자동으로 생성
@AllArgsConstructor // 모든 필드를 파라미터로 받는 생성자를 Lombok 라이브러리로 자동 생성
public class ResponseDto {
    
    private String code; // 응답 코드
    private String message; // 응답 메시지

    // 기본 생성자입니다. ResponseCode와 ResponseMessage에서 선언된 SUCCESS 상수를 사용
    public ResponseDto() {
        this.code = ResponseCode.SUCCESS;
        this.message = ResponseMessage.SUCCESS;
    }

    // ResponseCode와 ResponseMessage에서 선언된 DATABASE_ERROR 상수를 사용하여
    // 응답 코드와 메시지를 포함하는 ResponseDto 객체를 생성
    // 생성된 ResponseDto 객체를 바디로 하고, HTTP 상태 코드로 500(INTERNAL_SERVER_ERROR)를 사용하는
    // ResponseEntity 반환
    public static ResponseEntity<ResponseDto> databaseError(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.DATABASE_ERROR, ResponseMessage.DATABASE_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> validationFail(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.VALIDATION_FAIL, ResponseMessage.VALIDATION_FAIL);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
