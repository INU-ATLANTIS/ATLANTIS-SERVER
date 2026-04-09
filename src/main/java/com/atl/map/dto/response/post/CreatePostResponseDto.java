package com.atl.map.dto.response.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class CreatePostResponseDto extends ResponseDto {

    private Integer postId;

    private CreatePostResponseDto(Integer postId) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.postId = postId;
    }

    public static ResponseEntity<CreatePostResponseDto> success(Integer postId) {
        CreatePostResponseDto responseBody = new CreatePostResponseDto(postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
