package com.atl.map.dto.response.Noti;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;

public class PatchNotificationResponseDto extends ResponseDto {

    private PatchNotificationResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PatchNotificationResponseDto> success() {
        PatchNotificationResponseDto responseBody = new PatchNotificationResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
