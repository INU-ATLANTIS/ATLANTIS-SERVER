package com.atl.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.atl.map.entity.PostEntity;
import com.atl.map.repository.resultSet.GetPostResultSet;


@Repository
public interface PostRepository extends JpaRepository<PostEntity, Integer>{


    PostEntity findByPostId(Integer postId);

    @Query(value = "SELECT p.postId, p.title, p.content, " +
                   "IFNULL(p.updateDate, p.createDate) AS writeDatetime, " +
                   "p.userId AS writerUserId, u.nickname AS writerNickname, " +
                   "u.profileImage AS writerProfileImage " +
                   "FROM post p INNER JOIN user u ON p.userId = u.userId " +
                   "WHERE p.postId = ?1", nativeQuery = true)
    GetPostResultSet getPost(Integer postId);
    
}
