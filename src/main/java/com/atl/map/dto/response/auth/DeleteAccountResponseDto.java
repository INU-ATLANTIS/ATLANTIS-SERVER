package com.atl.map.dto.response.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class DeleteAccountResponseDto extends ResponseDto {

    public DeleteAccountResponseDto() {
        super();
    }

    public static ResponseEntity<DeleteAccountResponseDto> success() {
        DeleteAccountResponseDto responseBody = new DeleteAccountResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
