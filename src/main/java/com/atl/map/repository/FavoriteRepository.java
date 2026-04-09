package com.atl.map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.atl.map.entity.FavoriteEntity;
import com.atl.map.entity.primaryKey.FavoritePk;

import jakarta.transaction.Transactional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, FavoritePk>{
    
    FavoriteEntity findByPostIdAndUserId(Integer postId, Integer userId);
    @Transactional
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from favorite f where f.postId = :postId")
    void deleteAllByPostId(Integer postId);
    List<FavoriteEntity> findByUserId(Integer userId);
    void deleteByUserId(int userId);
}
