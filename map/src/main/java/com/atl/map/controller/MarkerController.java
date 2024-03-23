package com.atl.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atl.map.dto.response.marker.GetBuildingResponseDto;
import com.atl.map.service.MarkerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/marker")
@RequiredArgsConstructor
public class MarkerController {

    private final MarkerService markerService;

        
    @GetMapping("/building/{buildingId}")
    public ResponseEntity<? super GetBuildingResponseDto> getUser(
        @PathVariable("buildingId") Integer buildingId
    ){
        ResponseEntity<? super GetBuildingResponseDto> response = markerService.getBuilding(buildingId);
        return response;
    }
    
}
