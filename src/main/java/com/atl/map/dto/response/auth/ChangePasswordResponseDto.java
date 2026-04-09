package com.atl.map.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class ChangePasswordResponseDto extends ResponseDto {

    private ChangePasswordResponseDto() {
        super();
    }

    public static ResponseEntity<ChangePasswordResponseDto> success() {
        ChangePasswordResponseDto responseBody = new ChangePasswordResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
