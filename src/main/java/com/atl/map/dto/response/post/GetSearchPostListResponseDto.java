package com.atl.map.dto.response.post;

import java.util.List;

import org.springframework.data.domain.Page;
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
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;

    private GetSearchPostListResponseDto(Page<PostListViewEntity> postPage) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.searchList = PostListItem.getList(postPage.getContent());
        this.page = postPage.getNumber();
        this.size = postPage.getSize();
        this.totalPages = postPage.getTotalPages();
        this.totalElements = postPage.getTotalElements();
        this.hasNext = postPage.hasNext();
    }

    public static ResponseEntity<GetSearchPostListResponseDto> success(Page<PostListViewEntity> postPage) {
        GetSearchPostListResponseDto result = new GetSearchPostListResponseDto(postPage);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
