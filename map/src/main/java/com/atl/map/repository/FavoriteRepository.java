package com.atl.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atl.map.entity.FavoriteEntity;
import com.atl.map.entity.primaryKey.FavoritePk;

import jakarta.transaction.Transactional;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, FavoritePk>{
    
    FavoriteEntity findByPostIdAndUserId(Integer postId, Integer userId);
    @Transactional
    
    void deleteByPostId(Integer postId);
}
