package com.atl.map.entity;

import java.time.LocalDateTime;

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

    public CommentEntity(PostCommentRequestDto dto, Integer postId2, int userId2) {

        this.userId = userId2;
        this.postId = postId2;
        this.content = dto.getContent();
        this.createDate = LocalDateTime.now();
        this.likeCount = 0;

    }

}
