package com.atl.map.service;

import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.user.GetUserResponseDto;
import com.atl.map.dto.response.user.PatchNicknameResponseDto;
import com.atl.map.dto.request.user.PatchNicknameRequestDto;
import com.atl.map.dto.request.user.PatchProfileImageRequestDto;
import com.atl.map.dto.response.user.PatchProfileImageResponseDto;
import com.atl.map.dto.response.user.GetSignInUserResponseDto;

public interface UserService {
    
    ResponseEntity<? super GetUserResponseDto> getUser(String email);
    ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(String email);
    ResponseEntity<? super PatchNicknameResponseDto> patchNickname(PatchNicknameRequestDto dto, String email);
    ResponseEntity<? super PatchProfileImageResponseDto> patchProfileImage(PatchProfileImageRequestDto dto, String email);
}
