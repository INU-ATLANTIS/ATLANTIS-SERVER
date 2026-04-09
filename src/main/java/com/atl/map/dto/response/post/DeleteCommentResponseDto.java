package com.atl.map.dto.response.post;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class DeleteCommentResponseDto extends ResponseDto {

    public DeleteCommentResponseDto() {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
    }

    public static ResponseEntity<DeleteCommentResponseDto> success() {
        DeleteCommentResponseDto result = new DeleteCommentResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
