package com.atl.map.dto.object;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.event.PublicInvocationEvent;

import com.atl.map.entity.PostListViewEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostListItem {
    
    private int postId;
    private String title;
    private String content;
    private String postTitleImage;
    private int likeCount;
    private int commentCount;
    private String writeDatetime;
    private String writerNickname;
    private String writerProfileImage;
    
    public PostListItem(PostListViewEntity postListViewEntity){
        this.postId = postListViewEntity.getPostId();
        this.title = postListViewEntity.getTitle();
        this.content = postListViewEntity.getContent();
        this.postTitleImage = postListViewEntity.getPostTitleImage();
        this.likeCount = postListViewEntity.getLikeCount();
        this.commentCount = postListViewEntity.getCommentCount();
        this.writeDatetime = postListViewEntity.getWriteDatetime().toString();
        this.writerNickname = postListViewEntity.getWriterNickname();
        this.writerProfileImage = postListViewEntity.getWriterProfileImage();
    }

    public static List<PostListItem> getList(List<PostListViewEntity> postListViewEntities){
        List<PostListItem> list = new ArrayList<>();
        for(PostListViewEntity postListViewEntity : postListViewEntities){
            PostListItem postListItem = new PostListItem(postListViewEntity);
            list.add(postListItem);
        }
        return list;
    }
}
