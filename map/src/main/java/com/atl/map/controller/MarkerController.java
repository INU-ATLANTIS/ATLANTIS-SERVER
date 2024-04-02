package com.atl.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atl.map.dto.request.marker.CreateMarkerRequestDto;
import com.atl.map.dto.response.marker.*;
import com.atl.map.service.MarkerService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/marker")
@RequiredArgsConstructor
public class MarkerController {

    private final MarkerService markerService;

    @PostMapping("")
    public ResponseEntity<? super CreateMarkerResponseDto> createPost(
        @RequestBody @Valid CreateMarkerRequestDto requestBody,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super CreateMarkerResponseDto> response = markerService.createMarker(email, requestBody);
        return response;
    }
        
    @GetMapping("/building/{buildingId}")
    public ResponseEntity<? super GetBuildingResponseDto> getBuilding(
        @PathVariable("buildingId") Integer buildingId
    ){
        ResponseEntity<? super GetBuildingResponseDto> response = markerService.getBuilding(buildingId);
        return response;
    }

    @GetMapping("/building/building-list")
    public ResponseEntity<? super GetBuildingListResponseDto> getBuildingList(
    ){
        ResponseEntity<? super GetBuildingListResponseDto> response = markerService.getBuildingList();
        return response;
    }

    @GetMapping("/{markerId}")
    public ResponseEntity<? super GetMarkerResponseDto> getMarker(
    @PathVariable("markerId") Integer markerId
    ){
        return markerService.getMarker(markerId);
    }

}
