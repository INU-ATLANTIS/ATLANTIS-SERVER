package com.atl.map.service;

import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.auth.*;
import com.atl.map.dto.request.auth.*;

public interface AuthService {

    ResponseEntity<? super EmailCheckResponseDto> emailCheck(EmailCheckRequestDto dto);
    ResponseEntity<? super EmailCertificationResponseDto> emailCertificaion(EmailCertificationRequestDto dto);
    ResponseEntity<? super CheckCertificationResponseDto> checkCertification(CheckCertificationRequestDto dto);
    ResponseEntity<? super SignUpResponseDto> signUp (SignUpRequestDto dto);
    ResponseEntity<? super SignInResponseDto> signIn (SignInRequestDto dto);
    ResponseEntity<? super DeleteAccountResponseDto> deleteAccount(String email);
    ResponseEntity<? super ChangePasswordResponseDto> changePassword(ChangePasswordRequestDto dto);
    
}
