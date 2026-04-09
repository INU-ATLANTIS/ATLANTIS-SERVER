package com.atl.map.dto.response.Noti;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;

public class DeleteNotificationResponseDto extends ResponseDto {

    public DeleteNotificationResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<DeleteNotificationResponseDto> success() {
        DeleteNotificationResponseDto result = new DeleteNotificationResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
