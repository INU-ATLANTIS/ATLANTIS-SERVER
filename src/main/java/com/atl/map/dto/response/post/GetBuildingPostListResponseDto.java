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
public class GetBuildingPostListResponseDto extends ResponseDto{

    private List<PostListItem> buildingPostList;
    
    private GetBuildingPostListResponseDto(List<PostListViewEntity> postEntities){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.buildingPostList = PostListItem.getList(postEntities);
    }

    public static ResponseEntity<GetBuildingPostListResponseDto> success(List<PostListViewEntity> postEntities){
        GetBuildingPostListResponseDto result = new GetBuildingPostListResponseDto(postEntities);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    
}
