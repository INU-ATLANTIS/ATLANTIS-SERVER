package com.atl.map.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atl.map.config.EmailCertificationRateLimiter;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.dto.response.auth.*;
import com.atl.map.dto.request.auth.*;
import com.atl.map.exception.ErrorCode;
import com.atl.map.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController //request 바디 반환 컨트롤러
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final EmailCertificationRateLimiter emailCertificationRateLimiter;

    @PostMapping("/email-check")
    public ResponseEntity<? super EmailCheckResponseDto> emailCheck(
        @RequestBody @Valid EmailCheckRequestDto requestBody
    ){
        ResponseEntity<? super EmailCheckResponseDto> response = authService.emailCheck(requestBody);
        return response;
    }

    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(
        @RequestBody @Valid EmailCertificationRequestDto requestBody,
        HttpServletRequest request
    ){
        if (!emailCertificationRateLimiter.isAllowed(request)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new ResponseDto(
                            ErrorCode.TOO_MANY_REQUESTS.getCode(),
                            ErrorCode.TOO_MANY_REQUESTS.getMessage()));
        }

        ResponseEntity<? super EmailCertificationResponseDto> response = authService.emailCertificaion(requestBody);
        return response;
    }
    
    @PostMapping("/check-certification")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(
        @RequestBody @Valid CheckCertificationRequestDto requestBody
    ){
        ResponseEntity<? super CheckCertificationResponseDto> response = authService.checkCertification(requestBody);
        return response;
    }
    
    @PostMapping("/sign-up")
    public ResponseEntity<? super SignUpResponseDto> signUp (
       @RequestBody @Valid SignUpRequestDto requestBody)
    {
        ResponseEntity<? super SignUpResponseDto> response = authService.signUp(requestBody);
        return response;
    }

    @PostMapping("/sign-in")
    public ResponseEntity<? super SignInResponseDto> signIn (
        @RequestBody @Valid SignInRequestDto requestBody)
    {
        ResponseEntity<? super SignInResponseDto> response = authService.signIn(requestBody);
        return response;
    }

    @DeleteMapping("/delete-account")
    public ResponseEntity<? super DeleteAccountResponseDto> deleteAccount(
        @AuthenticationPrincipal String email) {
        ResponseEntity<? super DeleteAccountResponseDto> response = authService.deleteAccount(email);
        return response;
    }

    @PostMapping("/change-password")
    public ResponseEntity<? super ChangePasswordResponseDto> changePassword (
       @RequestBody @Valid ChangePasswordRequestDto requestBody)
    {
        ResponseEntity<? super ChangePasswordResponseDto> response = authService.changePassword(requestBody);
        return response;
    }
    
}
