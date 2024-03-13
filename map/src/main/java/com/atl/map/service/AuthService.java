package com.atl.map.service;

import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.auth.EmailCheckResponseDto;
import com.atl.map.dto.request.auth.CheckCertificationRequestDto;
import com.atl.map.dto.response.auth.CheckCertificationResponseDto;
import com.atl.map.dto.request.auth.EmailCertificationRequestDto;
import com.atl.map.dto.response.auth.EmailCertificationResponseDto;
import com.atl.map.dto.request.auth.EmailCheckRequestDto;
import com.atl.map.dto.request.auth.SignInRequestDto;
import com.atl.map.dto.response.auth.SignInResponseDto;
import com.atl.map.dto.request.auth.SignUpRequestDto;
import com.atl.map.dto.response.auth.SignUpResponseDto;

public interface AuthService {

    ResponseEntity<? super EmailCheckResponseDto> emailCheck(EmailCheckRequestDto dto);
    ResponseEntity<? super EmailCertificationResponseDto> emailCertificaion(EmailCertificationRequestDto dto);
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto);
    ResponseEntity<? super SignUpResponseDto> signUp (SignUpRequestDto dto);
    ResponseEntity<? super SignInResponseDto> signIn (SignInRequestDto dto);
}
