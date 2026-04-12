package com.atl.map.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.atl.map.dto.request.marker.CreateMarkerRequestDto;
import com.atl.map.dto.request.marker.PatchMarkerRequestDto;
import com.atl.map.dto.response.marker.*;
import com.atl.map.service.MarkerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/marker")
@RequiredArgsConstructor
@Tag(name = "Marker", description = "마커 및 건물 관련 API")
public class MarkerController {

    private final MarkerService markerService;

    @PostMapping("")
    @Operation(summary = "마커 생성", description = "게시글과 연결된 마커를 생성합니다.")
    public ResponseEntity<? super CreateMarkerResponseDto> createPost(
        @RequestBody @Valid CreateMarkerRequestDto requestBody,
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super CreateMarkerResponseDto> response = markerService.createMarker(email, requestBody);
        return response;
    }
        
    @GetMapping("/building/{buildingId}")
    @Operation(summary = "건물 상세 조회", description = "특정 건물의 상세 정보를 조회합니다.")
    public ResponseEntity<? super GetBuildingResponseDto> getBuilding(
        @PathVariable("buildingId") Integer buildingId
    ){
        ResponseEntity<? super GetBuildingResponseDto> response = markerService.getBuilding(buildingId);
        return response;
    }

    @GetMapping("/building/building-list")
    @Operation(summary = "건물 목록 조회", description = "전체 건물 목록을 조회합니다.")
    public ResponseEntity<? super GetBuildingListResponseDto> getBuildingList(
    ){
        ResponseEntity<? super GetBuildingListResponseDto> response = markerService.getBuildingList();
        return response;
    }

    @GetMapping("/{markerId}")
    @Operation(summary = "마커 상세 조회", description = "특정 마커의 상세 정보를 조회합니다.")
    public ResponseEntity<? super GetMarkerResponseDto> getMarker(
    @PathVariable("markerId") Integer markerId
    ){
        return markerService.getMarker(markerId);
    }

    @PatchMapping("/{markerId}")
    @Operation(summary = "마커 수정", description = "기존 마커 정보를 수정합니다.")
    public ResponseEntity<? super PatchMarekrResponseDto> updateMarker(
        @PathVariable("markerId") Integer markerId,
        @RequestBody @Valid PatchMarkerRequestDto requestBody,
        @AuthenticationPrincipal String email
    ){
        return markerService.patchMarker(requestBody, email, markerId);
    }

    @DeleteMapping("/{markerId}")
    @Operation(summary = "마커 삭제", description = "특정 마커를 삭제합니다.")
    public ResponseEntity<? super DeleteMarkerResponseDto> deleteMarker(
            @PathVariable("markerId") Integer markerId,
            @AuthenticationPrincipal String email) {
        return markerService.deleteMarker(markerId, email);
    }

    @GetMapping("/top")
    @Operation(summary = "인기 마커 조회", description = "최근 기간 기준 상위 마커 목록을 조회합니다.")
    public ResponseEntity<? super GetTopMarkerResponseDto> getMarkerList(
    ){
        ResponseEntity<? super GetTopMarkerResponseDto> response = markerService.getTopMarker();
        return response;
    }

    @GetMapping("/my")
    @Operation(summary = "내 마커 목록 조회", description = "현재 로그인 사용자가 생성한 마커 목록을 조회합니다.")
    public ResponseEntity<? super GetUserMarkerResponseDto> createPost(
        @AuthenticationPrincipal String email
    ){
        ResponseEntity<? super GetUserMarkerResponseDto> response = markerService.getUserMarker(email);
        return response;
    }

    @GetMapping("/search-building/{searchword}")
    @Operation(summary = "건물 검색", description = "건물명 키워드로 건물을 검색합니다.")
    public ResponseEntity<? super GetSearchBuildingResponseDto> getSearchBuilding(
        @PathVariable("searchword") String searchWord
    ){
        ResponseEntity<? super GetSearchBuildingResponseDto> response = markerService.getSearchBuildingId(searchWord);
        return response;
    }

    
    @GetMapping("{buildingId}/imagelist")
    @Operation(summary = "건물 이미지 목록 조회", description = "특정 건물의 이미지 목록을 조회합니다.")
    public ResponseEntity<? super GetBuildingImageResponseDto> getSearchBuilding(
        @PathVariable("buildingId") Integer buildingId
    ){
        ResponseEntity<? super GetBuildingImageResponseDto> response = markerService.getBuildingImage(buildingId);
        return response;
    }

}
