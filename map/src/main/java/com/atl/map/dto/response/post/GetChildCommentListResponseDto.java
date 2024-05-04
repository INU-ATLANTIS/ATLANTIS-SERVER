package com.atl.map.dto.response.post;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.object.CommentListItem;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.repository.resultSet.GetCommentListResultSet;

import lombok.Getter;

@Getter
public class GetChildCommentListResponseDto extends ResponseDto{
    
    private List<CommentListItem> commentList;

        private GetChildCommentListResponseDto(List<GetCommentListResultSet> resultSets){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.commentList = CommentListItem.copyList(resultSets);
    }

    public static ResponseEntity<GetChildCommentListResponseDto> success(List<GetCommentListResultSet> resultSets){
        GetChildCommentListResponseDto result = new GetChildCommentListResponseDto(resultSets);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    public static ResponseEntity<? super GetChildCommentListResponseDto> notExistComment() {
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_COMMENT, ResponseMessage.NOT_EXISTED_COMMENT);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

}
