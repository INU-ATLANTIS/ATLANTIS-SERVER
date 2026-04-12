package com.atl.map.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atl.map.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import com.atl.map.dto.request.user.*;
import com.atl.map.dto.response.user.*;
import org.springframework.web.bind.annotation.PostMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "사용자 프로필 및 신고 관련 API")
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @GetMapping("")
    @Operation(summary = "내 정보 조회", description = "현재 로그인 사용자의 정보를 조회합니다.")
    public ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(
        @AuthenticationPrincipal String userEmail
    ){
        ResponseEntity<? super GetSignInUserResponseDto> response = userService.getSignInUser(userEmail);
        return response;
    }
    
    @GetMapping("/{email}")
    @Operation(summary = "사용자 조회", description = "이메일 기준으로 사용자 프로필 정보를 조회합니다.")
    public ResponseEntity<? super GetUserResponseDto> getUser(
        @PathVariable("email") String email
    ){
        ResponseEntity<? super GetUserResponseDto> response = userService.getUser(email);
        return response;
    }

    @PatchMapping("/nickname")
    @Operation(summary = "닉네임 변경", description = "현재 로그인 사용자의 닉네임을 수정합니다.")
    public ResponseEntity<? super PatchNicknameResponseDto> patchNickname(
        @RequestBody @Valid PatchNicknameRequestDto requestBody,
        @AuthenticationPrincipal String email
    ){
        logger.info("Attempting to nickname for user: {}", email);
        ResponseEntity<? super PatchNicknameResponseDto> response = userService.patchNickname(requestBody, email);
        return response;
    }

    @PatchMapping("/profileimage")
    @Operation(summary = "프로필 이미지 변경", description = "현재 로그인 사용자의 프로필 이미지를 수정합니다.")
    public ResponseEntity<? super PatchProfileImageResponseDto> patchProfileImage(
        @RequestBody @Valid PatchProfileImageRequestDto requestBody,
        @AuthenticationPrincipal String email
    ){
        logger.info("Attempting to update profile image for user: {}", email);
        logger.debug("Profile image URL: {}", requestBody.getProfileImage());
        ResponseEntity<? super PatchProfileImageResponseDto> response = userService.patchProfileImage(requestBody, email);
        return response;
    }

    @PostMapping("/report/{userid}")
    @Operation(summary = "사용자 신고", description = "특정 사용자를 신고합니다.")
    public ResponseEntity<? super PostReportResponseDto> report(
        @PathVariable("userid") int userid,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super PostReportResponseDto> response = userService.report(email, userid);
        return response;
    }
    
}
