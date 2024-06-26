package com.atl.map.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.atl.map.entity.PostListViewEntity;

@Repository
public interface PostListViewRepository extends JpaRepository<PostListViewEntity, Integer> {
    
    List<PostListViewEntity> findByOrderByWriteDatetimeDesc();
    List<PostListViewEntity> findTop10ByWriteDatetimeGreaterThanOrderByLikeCountDescCommentCountDesc(LocalDateTime writeDatetime);
    List<PostListViewEntity> findByTitleContainsOrContentContainsOrderByWriteDatetimeDesc(String title, String content);
    List<PostListViewEntity> findByBuildingIdOrderByWriteDatetimeDesc(int buildingId);
    List<PostListViewEntity> findByUserIdOrderByWriteDatetimeDesc(int userId);
    
    @Query("SELECT p FROM postList p WHERE p.postId IN :ids")
    List<PostListViewEntity> findAllByPostIds(List<Integer> ids);

}
