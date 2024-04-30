package com.atl.map.dto.response.Noti;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.dto.response.Noti.CreateNotificationResponseDto;

import lombok.Getter;

@Getter
public class CreateNotificationResponseDto extends ResponseDto{

    private Integer NotificationId;

    private CreateNotificationResponseDto(Integer NotificationId){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.NotificationId = NotificationId;
    }

    public static ResponseEntity<CreateNotificationResponseDto> success(Integer NotificationId){
        CreateNotificationResponseDto responseBody = new CreateNotificationResponseDto(NotificationId);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notExistUser(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notExistMarker(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_MARKER, ResponseMessage.NOT_EXISTED_MARKER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
    
}
