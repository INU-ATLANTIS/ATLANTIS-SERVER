package com.atl.map.dto.response.post;

import java.util.List;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.entity.ImageEntity;
import com.atl.map.repository.resultSet.GetPostResultSet;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetPostResponseDto extends ResponseDto {
    private Integer postId;
    private String title;
    private String content;
    private List<String> postImageList; // 이미지 리스트는 null일 수도 있
    private String writeDatetime;
    private Integer writerUserId;
    private String writerNickname; // 작성자 닉네임은 null일 수도 있습니다.
    private String writerProfileImage; // 작성자 프로필 이미지 URL은 null일 수도 있습니다.

    public GetPostResponseDto(GetPostResultSet resultSet, List<ImageEntity> imageEntities) {
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        
        List<String> postImageList = new ArrayList<>();
        for(ImageEntity imageEntity: imageEntities){
            String postImage = imageEntity.getImage();
            postImageList.add(postImage);
            
        }
        this.postId = resultSet.getPostId();
        this.title = resultSet.getTitle();
        this.content = resultSet.getContent();
        this.postImageList = postImageList;
        this.writeDatetime = resultSet.getWriteDatetime();
        this.writerUserId = resultSet.getWriterUserId();
        this.writerNickname = resultSet.getWriterNickname();
        this.writerProfileImage = resultSet.getWriterProfileImage();
    }

    public static ResponseEntity<GetPostResponseDto> success(GetPostResultSet resultSet, List<ImageEntity> imageEntities){
        GetPostResponseDto responseBody = new GetPostResponseDto(resultSet, imageEntities);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notExistPost(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_POST, ResponseMessage.NOT_EXISTED_POST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
