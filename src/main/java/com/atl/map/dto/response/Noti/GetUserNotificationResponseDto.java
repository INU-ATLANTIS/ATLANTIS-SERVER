package com.atl.map.dto.response.Noti;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.ResponseDto;
import com.atl.map.entity.NotificationEntity;

import lombok.Getter;

@Getter
public class GetUserNotificationResponseDto extends ResponseDto {

    private List<NotificationEntity> UserNotiList;

    public GetUserNotificationResponseDto(List<NotificationEntity> UserNotiList) {
        super();
        this.UserNotiList = UserNotiList;
    }

    public static ResponseEntity<GetUserNotificationResponseDto> success(List<NotificationEntity> UserNotiList) {
        GetUserNotificationResponseDto result = new GetUserNotificationResponseDto(UserNotiList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
