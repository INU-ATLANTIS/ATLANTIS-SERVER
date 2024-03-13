package com.atl.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atl.map.dto.response.auth.EmailCertificationResponseDto;
import com.atl.map.dto.response.auth.EmailCheckResponseDto;
import com.atl.map.dto.request.auth.CheckCertificationRequestDto;
import com.atl.map.dto.response.auth.CheckCertificationResponseDto;
import com.atl.map.dto.request.auth.EmailCertificationRequestDto;
import com.atl.map.dto.request.auth.EmailCheckRequestDto;
import com.atl.map.dto.request.auth.SignInRequestDto;
import com.atl.map.dto.response.auth.SignInResponseDto;
import com.atl.map.dto.request.auth.SignUpRequestDto;
import com.atl.map.dto.response.auth.SignUpResponseDto;
import com.atl.map.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController //request 바디 반환 컨트롤러
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/email-check")
    public ResponseEntity<? super EmailCheckResponseDto> emailCheck(
        @RequestBody @Valid EmailCheckRequestDto requestBody
    ){
        ResponseEntity<? super EmailCheckResponseDto> response = authService.emailCheck(requestBody);
        return response;
    }

    @PostMapping("/email-certification")
    public ResponseEntity<? super EmailCertificationResponseDto> emailCertification(
        @RequestBody @Valid EmailCertificationRequestDto requestBody
    ){
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
    
}
