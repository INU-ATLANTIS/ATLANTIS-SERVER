package com.atl.map.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.atl.map.entity.MarkerEntity;

@Repository
public interface MarkerRepository extends JpaRepository<MarkerEntity, Integer>{
    
    void deleteByUserId(Integer userId);
    MarkerEntity findByMarkerId(Integer markerId);
    @Query(value = "SELECT m FROM marker m JOIN post p ON m.postId = p.postId WHERE COALESCE(p.updateDate, p.createDate) > :sinceDate ORDER BY p.likeCount DESC")
    List<MarkerEntity> findMarkersByLikesSinceDate(LocalDateTime sinceDate);
    List<MarkerEntity> findByUserId(int userId);

}
