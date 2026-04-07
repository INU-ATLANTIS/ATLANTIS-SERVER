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
public class GetTopPostListResponseDto extends ResponseDto{
    
    private List<PostListItem> topList;
    private GetTopPostListResponseDto(List<PostListViewEntity> postListViewEntities){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.topList = PostListItem.getList(postListViewEntities);
    }

    public static ResponseEntity<GetTopPostListResponseDto> success(List<PostListViewEntity> postListViewEntities){
        GetTopPostListResponseDto result = new GetTopPostListResponseDto(postListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
