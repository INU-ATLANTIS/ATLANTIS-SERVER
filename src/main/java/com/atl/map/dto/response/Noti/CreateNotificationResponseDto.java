package com.atl.map.dto.response.Noti;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class CreateNotificationResponseDto extends ResponseDto {

    private Integer NotificationId;

    private CreateNotificationResponseDto(Integer NotificationId) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.NotificationId = NotificationId;
    }

    public static ResponseEntity<CreateNotificationResponseDto> success(Integer NotificationId) {
        CreateNotificationResponseDto responseBody = new CreateNotificationResponseDto(NotificationId);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
