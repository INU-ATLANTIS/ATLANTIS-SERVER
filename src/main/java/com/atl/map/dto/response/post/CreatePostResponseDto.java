package com.atl.map.dto.response.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class CreatePostResponseDto extends ResponseDto{
    private Integer postId;
    
    private CreatePostResponseDto(Integer postId){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.postId = postId;
    }

    public static ResponseEntity<CreatePostResponseDto> success(Integer postId){
        CreatePostResponseDto responseBody = new CreatePostResponseDto(postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notExistUser(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> reportedUser(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.REPORTED_USER, ResponseMessage.REPORTED_USER);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
