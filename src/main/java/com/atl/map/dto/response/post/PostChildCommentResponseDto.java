package com.atl.map.dto.response.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;

public class PostChildCommentResponseDto extends ResponseDto {

    private PostChildCommentResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<PostChildCommentResponseDto> success() {
        PostChildCommentResponseDto responseBody = new PostChildCommentResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
