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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController //request 바디 반환 컨트롤러
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "인증 및 계정 관련 API")
public class AuthController {

    private final AuthService authService;
    private final EmailCertificationRateLimiter emailCertificationRateLimiter;

    @PostMapping("/email-check")
    @Operation(summary = "이메일 중복 확인", description = "회원가입 전 이메일 사용 가능 여부를 확인합니다.")
    public ResponseEntity<? super EmailCheckResponseDto> emailCheck(
        @RequestBody @Valid EmailCheckRequestDto requestBody
    ){
        ResponseEntity<? super EmailCheckResponseDto> response = authService.emailCheck(requestBody);
        return response;
    }

    @PostMapping("/email-certification")
    @Operation(summary = "이메일 인증번호 발송", description = "인증번호를 발급해 메일로 전송합니다. 요청 제한이 적용됩니다.")
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
    @Operation(summary = "이메일 인증번호 확인", description = "전송된 인증번호가 일치하는지 검증합니다.")
    public ResponseEntity<? super CheckCertificationResponseDto> checkCertification(
        @RequestBody @Valid CheckCertificationRequestDto requestBody
    ){
        ResponseEntity<? super CheckCertificationResponseDto> response = authService.checkCertification(requestBody);
        return response;
    }
    
    @PostMapping("/sign-up")
    @Operation(summary = "회원가입", description = "이메일 인증 완료 후 신규 사용자를 등록합니다.")
    public ResponseEntity<? super SignUpResponseDto> signUp (
       @RequestBody @Valid SignUpRequestDto requestBody)
    {
        ResponseEntity<? super SignUpResponseDto> response = authService.signUp(requestBody);
        return response;
    }

    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "로그인 후 access token과 refresh token을 발급합니다.")
    public ResponseEntity<? super SignInResponseDto> signIn (
        @RequestBody @Valid SignInRequestDto requestBody)
    {
        ResponseEntity<? super SignInResponseDto> response = authService.signIn(requestBody);
        return response;
    }

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급", description = "refresh token을 검증해 access token과 refresh token을 재발급합니다.")
    public ResponseEntity<? super ReissueTokenResponseDto> reissue(
        @RequestBody @Valid ReissueTokenRequestDto requestBody
    ) {
        ResponseEntity<? super ReissueTokenResponseDto> response = authService.reissue(requestBody);
        return response;
    }

    @DeleteMapping("/delete-account")
    @Operation(summary = "회원 탈퇴", description = "현재 로그인 사용자를 탈퇴 처리하고 refresh token을 무효화합니다.")
    public ResponseEntity<? super DeleteAccountResponseDto> deleteAccount(
        @AuthenticationPrincipal String email) {
        ResponseEntity<? super DeleteAccountResponseDto> response = authService.deleteAccount(email);
        return response;
    }

    @PostMapping("/change-password")
    @Operation(summary = "비밀번호 변경", description = "이메일 인증 완료 후 비밀번호를 변경하고 refresh token을 무효화합니다.")
    public ResponseEntity<? super ChangePasswordResponseDto> changePassword (
       @RequestBody @Valid ChangePasswordRequestDto requestBody)
    {
        ResponseEntity<? super ChangePasswordResponseDto> response = authService.changePassword(requestBody);
        return response;
    }
    
}
