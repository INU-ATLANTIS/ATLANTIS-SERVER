package com.atl.map.service;

import org.springframework.http.ResponseEntity;

import com.atl.map.dto.request.user.*;
import com.atl.map.dto.response.user.*;

public interface UserService {
    
    ResponseEntity<? super GetUserResponseDto> getUser(String email);
    ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(String email);
    ResponseEntity<? super PatchNicknameResponseDto> patchNickname(PatchNicknameRequestDto dto, String email);
    ResponseEntity<? super PatchProfileImageResponseDto> patchProfileImage(PatchProfileImageRequestDto dto, String email);
    ResponseEntity<? super PostReportResponseDto> report(String email, int userid);
}
