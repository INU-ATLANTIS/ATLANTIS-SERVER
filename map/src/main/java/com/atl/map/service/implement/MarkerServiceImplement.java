package com.atl.map.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.atl.map.dto.response.ResponseDto;
import com.atl.map.dto.response.marker.GetBuildingResponseDto;
import com.atl.map.dto.response.user.GetUserResponseDto;
import com.atl.map.entity.BuildingEntity;
import com.atl.map.entity.UserEntity;
import com.atl.map.repository.BuildingRepository;
import com.atl.map.service.MarkerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MarkerServiceImplement implements MarkerService{

    private final BuildingRepository buildingRepository;
    @Override
    public ResponseEntity<? super GetBuildingResponseDto> getBuilding(Integer buildingId) {
        BuildingEntity buildingEntity = null;

        try{

            buildingEntity = buildingRepository.findByBuildingId(buildingId);
           if(buildingEntity == null) return ResponseDto.databaseError();

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetBuildingResponseDto.success(buildingEntity);
    }
    
}
