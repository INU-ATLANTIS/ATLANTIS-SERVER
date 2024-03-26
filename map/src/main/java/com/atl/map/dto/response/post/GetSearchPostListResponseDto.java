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
public class GetSearchPostListResponseDto extends ResponseDto {
    
    private List<PostListItem> searchList;
    private GetSearchPostListResponseDto(List<PostListViewEntity> postListViewEntities){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.searchList = PostListItem.getList(postListViewEntities);
    }

    public static ResponseEntity<GetSearchPostListResponseDto> success(List<PostListViewEntity> postListViewEntities){
            GetSearchPostListResponseDto result = new GetSearchPostListResponseDto(postListViewEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
