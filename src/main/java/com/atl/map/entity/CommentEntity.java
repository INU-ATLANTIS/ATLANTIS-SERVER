package com.atl.map.entity;

import java.time.LocalDateTime;

import com.atl.map.dto.request.post.PostChildCommentRequestDto;
import com.atl.map.dto.request.post.PostCommentRequestDto;

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
@Entity(name="comment")
@Table(name="comment")
public class CommentEntity {

    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int commentId;
    private int userId;
    private int postId;
    private String content;
    private Integer likeCount;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Integer parentId;

    public CommentEntity(PostCommentRequestDto dto, Integer postId, int userId) {

        this.userId = userId;
        this.postId = postId;
        this.content = dto.getContent();
        this.createDate = LocalDateTime.now();
        this.likeCount = 0;

    }

    public CommentEntity(PostChildCommentRequestDto dto, Integer postId, Integer parentId, int userId) {

        this.parentId = parentId;
        this.userId = userId;
        this.postId = postId;
        this.content = dto.getContent();
        this.createDate = LocalDateTime.now();
        this.likeCount = 0;

    }

}
