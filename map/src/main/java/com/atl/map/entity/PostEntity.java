package com.atl.map.entity;

import java.time.LocalDateTime;

import com.atl.map.dto.request.post.CreatePostRequestDto;
import com.atl.map.dto.request.post.PatchPostRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="post")
@Table(name="post")
public class PostEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // IDENTITY 전략 사용
    @Column(name = "postId")
    private int postId;
    private String title;
    private String content;
    private int likeCount;
    private int commentCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private int userId;
    private Integer buildingId;

    public PostEntity(CreatePostRequestDto dto, int Id) {
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.createDate = LocalDateTime.now();
        this.commentCount = 0;
        this.likeCount = 0;
        this.userId = Id;
        this.buildingId = (dto.getBuildingId() != 0) ? dto.getBuildingId() : null; // 조건부로 null 할당
    }

    public void increaseLikeCount(){
        this.likeCount++;
    }

    public void decreaseLikeCount(){
        this.likeCount--;
    }

    public void increaseCommentCount(){
        this.commentCount++;
    }

    public void decreaseCommentCount(){
        this.commentCount--;
    }

    public void decreaseCommentCount(int count) {
        this.commentCount -= count;
        if (this.commentCount < 0) {
            this.commentCount = 0; // 댓글 수가 음수가 되지 않도록 방지
        }
    }

    public void patchPost(PatchPostRequestDto dto){
        this.title = dto.getTitle();
        this.content = dto.getContent();
        this.updateDate = LocalDateTime.now();
    }

    
}
