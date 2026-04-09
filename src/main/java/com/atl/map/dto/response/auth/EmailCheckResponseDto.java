package com.atl.map.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class EmailCheckResponseDto extends ResponseDto {

    private EmailCheckResponseDto() {
        super();
    }

    public static ResponseEntity<EmailCheckResponseDto> success() {
        EmailCheckResponseDto responseBody = new EmailCheckResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
