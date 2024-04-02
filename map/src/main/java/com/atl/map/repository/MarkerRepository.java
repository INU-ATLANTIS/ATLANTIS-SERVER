package com.atl.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atl.map.entity.MarkerEntity;

@Repository
public interface MarkerRepository extends JpaRepository<MarkerEntity, Integer>{
    
    void deleteByUserId(Integer userId);
    MarkerEntity findByMarkerId(Integer markerId);
}
