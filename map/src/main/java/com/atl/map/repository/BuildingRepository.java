package com.atl.map.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.atl.map.entity.BuildingEntity;

@Repository
public interface BuildingRepository extends JpaRepository<BuildingEntity, Integer> {

    BuildingEntity findByBuildingId(Integer buildingId);

    @Query(value="SELECT * FROM building", nativeQuery = true)
    List<BuildingEntity> getBuildingList();
    BuildingEntity findByBuildingCodeContains(String code);
    
}
