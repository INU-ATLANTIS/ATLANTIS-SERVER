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
public class GetMyLikePostResponseDto extends ResponseDto {
    
        private List<PostListItem> myLikePosts;

    private GetMyLikePostResponseDto(List<PostListViewEntity> postListViewEntities){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.myLikePosts = PostListItem.getList(postListViewEntities);
    }

    public static ResponseEntity<GetMyLikePostResponseDto> success(List<PostListViewEntity> postListViewEntities){
        GetMyLikePostResponseDto result = new GetMyLikePostResponseDto(postListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistUser(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
