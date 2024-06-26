package com.atl.map.dto.object;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.atl.map.repository.resultSet.GetCommentListResultSet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentListItem {
    
    private String nickname;
    private String profileImage;
    private LocalDateTime writeDatetime;
    private String content;
    private Integer parentId;
    private Integer commentId;

    public CommentListItem(GetCommentListResultSet resultSet){
        this.nickname = resultSet.getNickname();
        this.profileImage = resultSet.getProfileImage();
        this.writeDatetime = resultSet.getWriteDatetime();
        this.content = resultSet.getContent();
        this.parentId = resultSet.getParentId();
        this.commentId = resultSet.getCommentId();
    }

    public static List<CommentListItem> copyList(List<GetCommentListResultSet> resultSets){

        List<CommentListItem> list = new ArrayList<>();
        for(GetCommentListResultSet resultSet : resultSets){
            CommentListItem commentListItem = new CommentListItem(resultSet);
            list.add(commentListItem);
        }
        return list;
    }
}
