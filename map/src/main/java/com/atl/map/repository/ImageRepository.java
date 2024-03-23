package com.atl.map.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atl.map.entity.ImageEntity;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity, Integer>{


    
}
