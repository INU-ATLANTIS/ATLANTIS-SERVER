package com.atl.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.atl.map.entity.PostEntity;
import com.atl.map.repository.resultSet.GetPostResultSet;

import jakarta.transaction.Transactional;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer>{


    PostEntity findByPostId(Integer postId);

    @Query(value = "SELECT p.postId, p.title, p.content, " +
                   "IFNULL(p.updateDate, p.createDate) AS writeDatetime, " +
                   "p.userId AS writerUserId, u.nickname AS writerNickname, " +
                   "u.profileImage AS writerProfileImage, " +
                   "p.likeCount, " +
                   "p.commentCount, " +
                   "p.buildingId " +
                   "FROM post p INNER JOIN user u ON p.userId = u.userId " +
                   "WHERE p.postId = ?1", nativeQuery = true)
    GetPostResultSet getPost(Integer postId);
    void deleteByUserId(Integer userId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update post p set p.likeCount = p.likeCount + 1 where p.postId = :postId")
    int increaseLikeCount(Integer postId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update post p set p.likeCount = case when p.likeCount > 0 then p.likeCount - 1 else 0 end where p.postId = :postId")
    int decreaseLikeCount(Integer postId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update post p set p.commentCount = p.commentCount + 1 where p.postId = :postId")
    int increaseCommentCount(Integer postId);

    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update post p set p.commentCount = case when p.commentCount >= :count then p.commentCount - :count else 0 end where p.postId = :postId")
    int decreaseCommentCount(Integer postId, int count);
}
