package com.atl.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.atl.map.dto.request.Noti.CreateNotificationRequestDto;
import com.atl.map.dto.request.Noti.PatchNotificationRequestDto;
import com.atl.map.dto.response.Noti.CreateNotificationResponseDto;
import com.atl.map.dto.response.Noti.DeleteNotificationResponseDto;
import com.atl.map.dto.response.Noti.GetNotificationResponseDto;
import com.atl.map.dto.response.Noti.GetUserNotificationResponseDto;
import com.atl.map.dto.response.Noti.PatchNotificationResponseDto;
import com.atl.map.service.NotiService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/noti")
@RequiredArgsConstructor
@Tag(name = "Notification", description = "알림 설정 관련 API")
public class NotiController {

    private final NotiService notiService;

    @PostMapping("")
    @Operation(summary = "알림 생성", description = "새 알림 설정을 생성합니다.")
    public ResponseEntity<? super CreateNotificationResponseDto> createNotification(
        @AuthenticationPrincipal String email,
        @RequestBody @Valid CreateNotificationRequestDto requestBody
    ) {
        return notiService.createNotification(email, requestBody);
    }

    @DeleteMapping("/delete/{notiId}")
    @Operation(summary = "알림 삭제", description = "특정 알림 설정을 삭제합니다.")
    public ResponseEntity<? super DeleteNotificationResponseDto> deleteNotification(
        @AuthenticationPrincipal String email,
        @PathVariable int notiId
    ) {
        return notiService.deleteNotification(email, notiId);
    }

    @PatchMapping("/update/{notiId}")
    @Operation(summary = "알림 수정", description = "기존 알림 설정을 수정합니다.")
    public ResponseEntity<? super PatchNotificationResponseDto> updateNotification(
        @AuthenticationPrincipal String email,
        @PathVariable int notiId,
        @RequestBody @Valid PatchNotificationRequestDto requestBody
    ) {
        return notiService.patchNotification(requestBody, notiId, email);
    }

    @GetMapping("/{notiId}")
    @Operation(summary = "알림 상세 조회", description = "특정 알림 설정 정보를 조회합니다.")
    public ResponseEntity<? super GetNotificationResponseDto> getNotification(
        @PathVariable int notiId
    ) {
        return notiService.getNotification(notiId);
    }

    @GetMapping("/my")
    @Operation(summary = "내 알림 목록 조회", description = "현재 로그인 사용자의 알림 설정 목록을 조회합니다.")
    public ResponseEntity<? super GetUserNotificationResponseDto> getUserNotifications(
        @AuthenticationPrincipal String email
    ) {
        return notiService.getUserNotification(email);
    }
}
