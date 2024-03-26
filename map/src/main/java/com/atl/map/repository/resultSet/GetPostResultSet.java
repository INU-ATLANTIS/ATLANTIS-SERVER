package com.atl.map.repository.resultSet;

public interface GetPostResultSet {
    Integer getPostId();
    String getTitle();
    String getContent();
    String getWriteDatetime(); 
    Integer getWriterUserId();
    String getWriterNickname();
    String getWriterProfileImage();
}
