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

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/noti")
@RequiredArgsConstructor
public class NotiController {

    private final NotiService notiService;

    @PostMapping("")
    public ResponseEntity<? super CreateNotificationResponseDto> createNotification(
        @AuthenticationPrincipal String email,
        @RequestBody @Valid CreateNotificationRequestDto requestBody
    ) {
        return notiService.createNotification(email, requestBody);
    }

    @DeleteMapping("/delete/{notiId}")
    public ResponseEntity<? super DeleteNotificationResponseDto> deleteNotification(
        @AuthenticationPrincipal String email,
        @PathVariable int notiId
    ) {
        return notiService.deleteNotification(email, notiId);
    }

    @PatchMapping("/update/{notiId}")
    public ResponseEntity<? super PatchNotificationResponseDto> updateNotification(
        @AuthenticationPrincipal String email,
        @PathVariable int notiId,
        @RequestBody @Valid PatchNotificationRequestDto requestBody
    ) {
        return notiService.patchNotification(requestBody, notiId, email);
    }

    @GetMapping("/{notiId}")
    public ResponseEntity<? super GetNotificationResponseDto> getNotification(
        @PathVariable int notiId
    ) {
        return notiService.getNotification(notiId);
    }

    @GetMapping("/my")
    public ResponseEntity<? super GetUserNotificationResponseDto> getUserNotifications(
        @AuthenticationPrincipal String email
    ) {
        return notiService.getUserNotification(email);
    }
}
