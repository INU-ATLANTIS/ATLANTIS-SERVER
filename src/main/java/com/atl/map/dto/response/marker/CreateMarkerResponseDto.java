package com.atl.map.dto.response.marker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class CreateMarkerResponseDto extends ResponseDto {

    private CreateMarkerResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<CreateMarkerResponseDto> success() {
        CreateMarkerResponseDto responseBody = new CreateMarkerResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
