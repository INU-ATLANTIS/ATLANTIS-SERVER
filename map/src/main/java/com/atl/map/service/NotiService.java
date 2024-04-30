package com.atl.map.service;

import org.springframework.http.ResponseEntity;

import com.atl.map.dto.request.Noti.CreateNotificationRequestDto;
import com.atl.map.dto.request.Noti.PatchNotificationRequestDto;
import com.atl.map.dto.response.Noti.*;

public interface NotiService {
    ResponseEntity<? super CreateNotificationResponseDto> createNotification(String email, CreateNotificationRequestDto dto);
    ResponseEntity<? super DeleteNotificationResponseDto> deleteNotification(String email, int notiId);
    ResponseEntity<? super PatchNotificationResponseDto> patchNotification(PatchNotificationRequestDto dto, int notiId, String email);
    ResponseEntity<? super GetNotificationResponseDto> getNotification(int notiId);
    ResponseEntity<? super GetUserNotificationResponseDto> getUserNotification(String email);
}
