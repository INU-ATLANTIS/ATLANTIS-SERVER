package com.atl.map.dto.response.post;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.object.PostListItem;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.entity.PostListViewEntity;

import lombok.Getter;

@Getter
public class GetMyPostResponseDto extends ResponseDto{
    
    private List<PostListItem> myPosts;

    private GetMyPostResponseDto(List<PostListViewEntity> postListViewEntities){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.myPosts = PostListItem.getList(postListViewEntities);
    }

    public static ResponseEntity<GetMyPostResponseDto> success(List<PostListViewEntity> postListViewEntities){
        GetMyPostResponseDto result = new GetMyPostResponseDto(postListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistUser(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
