package com.atl.map.repository.resultSet;

import java.time.LocalDateTime;

public interface GetCommentListResultSet {

    String getNickname();
    String getProfileImage();
    LocalDateTime getWriteDatetime();
    String getContent();
    
}
