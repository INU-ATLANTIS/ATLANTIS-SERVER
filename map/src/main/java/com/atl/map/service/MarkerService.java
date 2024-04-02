package com.atl.map.service;

import org.springframework.http.ResponseEntity;

import com.atl.map.dto.request.marker.CreateMarkerRequestDto;
import com.atl.map.dto.response.marker.*;

public interface MarkerService {
    ResponseEntity<? super GetBuildingResponseDto> getBuilding(Integer buildingId);

    ResponseEntity<? super GetBuildingListResponseDto> getBuildingList();

    ResponseEntity<? super CreateMarkerResponseDto> createMarker(String email, CreateMarkerRequestDto dto);

    ResponseEntity<? super GetMarkerResponseDto> getMarker(Integer markerId);

}
