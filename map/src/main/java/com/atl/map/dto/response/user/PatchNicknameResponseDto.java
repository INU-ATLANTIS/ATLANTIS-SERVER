package com.atl.map.dto.response.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class PatchNicknameResponseDto extends ResponseDto{
    
    private PatchNicknameResponseDto(){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PatchNicknameResponseDto> success(){
        PatchNicknameResponseDto responseBody = new PatchNicknameResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> noExistUser(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> duplicateNickname(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.DUPLICATE_NICKNAME, ResponseMessage.DUPLICATE_NICKNMAE);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
