package com.atl.map.dto.response.marker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;

public class DeleteMarkerResponseDto extends ResponseDto {

    public DeleteMarkerResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<DeleteMarkerResponseDto> success() {
        DeleteMarkerResponseDto result = new DeleteMarkerResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
