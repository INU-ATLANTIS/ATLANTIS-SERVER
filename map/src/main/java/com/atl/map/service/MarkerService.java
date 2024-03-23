package com.atl.map.service;

import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.marker.*;

public interface MarkerService {
    ResponseEntity<? super GetBuildingResponseDto> getBuilding(Integer buildingId);

}
