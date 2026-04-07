package com.atl.map.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.atl.map.dto.response.ResponseDto;

// 이 클래스가 전역 예외 핸들러 역할을 수행하도록 지정
// 즉, 이 클래스는 컨트롤러에서 발생하는 예외를 처리하는 공통의 로직을 제공
@RestControllerAdvice
public class ValidationExceptionHandler {
    
    // @ExceptionHandler 어노테이션을 사용하여 처리할 예외 유형을 지정
    // 두 예외는 주로 클라이언트로부터 전달받은 요청의 형식이 유효하지 않을 때 발생
    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class })
    public ResponseEntity<ResponseDto> validationExceptionHandler(Exception exception){
        
        // 예외가 발생했을 때 클라이언트에게 반환할 응답을 구성
        // 유효성 검사 실패 시 적절한 응답 코드와 메시지를 포함한 ResponseDto 객체를 반환
        return ResponseDto.validationFail();
    }
}
