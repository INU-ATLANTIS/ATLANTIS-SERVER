package com.atl.map.dto.response.post;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.entity.CommentEntity;


import lombok.Getter;

@Getter
public class GetMyCommentListResponseDto extends ResponseDto{
    private List<CommentEntity> myComments;

    private GetMyCommentListResponseDto(List<CommentEntity> myComments){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.myComments = myComments;
    }

    public static ResponseEntity<GetMyCommentListResponseDto> success(List<CommentEntity> myComments){
        GetMyCommentListResponseDto result = new GetMyCommentListResponseDto(myComments);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistUser(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
