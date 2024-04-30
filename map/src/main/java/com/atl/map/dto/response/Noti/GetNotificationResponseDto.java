package com.atl.map.dto.response.Noti;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.entity.NotificationEntity;

@Getter
@RequiredArgsConstructor
public class GetNotificationResponseDto extends ResponseDto{
    private final int userId;
    private final Integer markerId;
    private final String notiMessage;
    private final Integer radius;
    private final int type;
    private final LocalDateTime dateTime;
    private final LocalDateTime crateTime;
    private final LocalDateTime upDateTime;

    public GetNotificationResponseDto(NotificationEntity notificationEntity){
        super();
        this.userId = notificationEntity.getUserId();
        this.dateTime = notificationEntity.getDateTime();
        this.notiMessage = notificationEntity.getMessage();
        this.markerId = notificationEntity.getMarkerId();
        this.type = notificationEntity.getType();
        this.radius = notificationEntity.getRadius();
        this.crateTime = notificationEntity.getCreateDate();
        this.upDateTime = notificationEntity.getUpdateDate();
    }

    public static ResponseEntity<GetNotificationResponseDto> success(NotificationEntity notificationEntity){
        GetNotificationResponseDto result = new GetNotificationResponseDto(notificationEntity);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistNotification(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_NOTIFICATION, ResponseMessage.NOT_EXISTED_NOTIFICATION);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> noPermisson(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NO_PERMISSION, ResponseMessage.NO_PERMISSION);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(responseBody);
    }
}
