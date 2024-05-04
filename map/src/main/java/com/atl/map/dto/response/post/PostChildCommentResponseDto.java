package com.atl.map.dto.response.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;


public class PostChildCommentResponseDto extends ResponseDto {
    
        private PostChildCommentResponseDto(){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PostChildCommentResponseDto> success(){
        PostChildCommentResponseDto responseBody = new PostChildCommentResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notExistPost(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_POST, ResponseMessage.NOT_EXISTED_POST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notExistUser(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseBody);
    }
}
