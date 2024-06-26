package com.atl.map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.atl.map.entity.CommentEntity;
import com.atl.map.repository.resultSet.GetCommentListResultSet;

import jakarta.transaction.Transactional;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    
    @Transactional
    void deleteByPostId(Integer postId);

    @Query(
        value="SELECT " + 
                    "u.nickname, " + 
                    "u.profileImage, " +
                    "IFNULL(c.updateDate, c.createDate) AS writeDatetime, " +
                    "c.content, " + 
                    "c.parentId, " +
                    "c.commentId " +
                    "FROM comment AS c " + 
                    "JOIN user AS u ON c.userId = u.userId " + 
                    "WHERE c.postId = ?1 " +
                    "ORDER BY writeDatetime", 
        nativeQuery = true
    )
    List<GetCommentListResultSet> getCommentList(Integer postId);

    @Query(
        value="SELECT " + 
                    "u.nickname, " + 
                    "u.profileImage, " +
                    "IFNULL(c.updateDate, c.createDate) AS writeDatetime, " +
                    "c.content, " + 
                    "c.parentId, " +
                    "c.commentId " +
                    "FROM comment AS c " + 
                    "JOIN user AS u ON c.userId = u.userId " + 
                    "WHERE c.parentId = ?1 " +
                    "ORDER BY writeDatetime", 
        nativeQuery = true
    )
    List<GetCommentListResultSet> getChildCommentList(Integer commentId);

    void deleteByUserId(int userId);
    List<CommentEntity> findByUserId(int userId);
    CommentEntity findByCommentId(int commentId);
    List<CommentEntity> findByParentId(Integer parentId);
}
