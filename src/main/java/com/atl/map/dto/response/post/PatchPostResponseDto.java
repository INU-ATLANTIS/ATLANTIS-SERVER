package com.atl.map.dto.response.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class PatchPostResponseDto extends ResponseDto {

    private PatchPostResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PatchPostResponseDto> success() {
        PatchPostResponseDto responseBody = new PatchPostResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
