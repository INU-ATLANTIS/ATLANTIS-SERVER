package com.atl.map.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="postList")
@Table(name="postList")
public class PostListViewEntity {
    
    @Id
    private int postId;
    private String title;
    private String content;
    private int likeCount;
    private int commentCount;
    private LocalDateTime writeDatetime; 
    private String writerNickname;
    private String writerProfileImage;
    private String postTitleImage; 
}
