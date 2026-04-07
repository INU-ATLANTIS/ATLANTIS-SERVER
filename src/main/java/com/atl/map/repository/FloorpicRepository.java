package com.atl.map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.atl.map.entity.FloorpicEntity;

@Repository
public interface FloorpicRepository extends JpaRepository<FloorpicEntity, Integer> {
    
    List<FloorpicEntity> findByBuildingId(Integer buildingId);
}
