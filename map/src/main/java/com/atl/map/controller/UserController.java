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



@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {
    
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(
        @AuthenticationPrincipal String userEmail
    ){
        ResponseEntity<? super GetSignInUserResponseDto> response = userService.getSignInUser(userEmail);
        return response;
    }
    
    @GetMapping("/{email}")
    public ResponseEntity<? super GetUserResponseDto> getUser(
        @PathVariable("email") String email
    ){
        ResponseEntity<? super GetUserResponseDto> response = userService.getUser(email);
        return response;
    }

    @PatchMapping("/nickname")
    public ResponseEntity<? super PatchNicknameResponseDto> patchNickname(
        @RequestBody @Valid PatchNicknameRequestDto requestBody,
        @AuthenticationPrincipal String email
    ){
        logger.info("Attempting to nickname for user: {}", email);
        ResponseEntity<? super PatchNicknameResponseDto> response = userService.patchNickname(requestBody, email);
        return response;
    }

    @PatchMapping("/profileimage")
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
    public ResponseEntity<? super PostReportResponseDto> report(
        @PathVariable("userid") int userid,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super PostReportResponseDto> response = userService.report(email, userid);
        return response;
    }
    
}