package com.atl.map.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.atl.map.entity.PostListViewEntity;

@Repository
public interface PostListViewRepository extends JpaRepository<PostListViewEntity, Integer> {
    
    Page<PostListViewEntity> findByOrderByWriteDatetimeDescPostIdDesc(Pageable pageable);
    List<PostListViewEntity> findTop10ByWriteDatetimeGreaterThanOrderByLikeCountDescCommentCountDesc(LocalDateTime writeDatetime);
    Page<PostListViewEntity> findByTitleContainsOrContentContainsOrderByWriteDatetimeDescPostIdDesc(
            String title, String content, Pageable pageable);
    Page<PostListViewEntity> findByBuildingIdOrderByWriteDatetimeDescPostIdDesc(int buildingId, Pageable pageable);
    List<PostListViewEntity> findByUserIdOrderByWriteDatetimeDesc(int userId);
    
    @Query("SELECT p FROM postList p WHERE p.postId IN :ids")
    List<PostListViewEntity> findAllByPostIds(List<Integer> ids);

}
