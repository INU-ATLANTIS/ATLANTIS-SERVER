package com.atl.map.service.implement;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.atl.map.dto.request.marker.CreateMarkerRequestDto;
import com.atl.map.dto.request.marker.PatchMarkerRequestDto;
import com.atl.map.dto.response.marker.CreateMarkerResponseDto;
import com.atl.map.dto.response.marker.DeleteMarkerResponseDto;
import com.atl.map.dto.response.marker.GetBuildingImageResponseDto;
import com.atl.map.dto.response.marker.GetBuildingListResponseDto;
import com.atl.map.dto.response.marker.GetBuildingResponseDto;
import com.atl.map.dto.response.marker.GetMarkerResponseDto;
import com.atl.map.dto.response.marker.GetSearchBuildingResponseDto;
import com.atl.map.dto.response.marker.GetTopMarkerResponseDto;
import com.atl.map.dto.response.marker.GetUserMarkerResponseDto;
import com.atl.map.dto.response.marker.PatchMarekrResponseDto;
import com.atl.map.entity.BuildingEntity;
import com.atl.map.entity.FloorpicEntity;
import com.atl.map.entity.MarkerEntity;
import com.atl.map.entity.PostEntity;
import com.atl.map.entity.UserEntity;
import com.atl.map.exception.BusinessException;
import com.atl.map.exception.ErrorCode;
import com.atl.map.repository.BuildingRepository;
import com.atl.map.repository.FloorpicRepository;
import com.atl.map.repository.MarkerRepository;
import com.atl.map.repository.NotificationRepository;
import com.atl.map.repository.PostRepository;
import com.atl.map.repository.UserRepository;
import com.atl.map.service.MarkerService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MarkerServiceImplement implements MarkerService {

    private final UserRepository userRepository;
    private final MarkerRepository markerRepository;
    private final PostRepository postRepository;
    private final BuildingRepository buildingRepository;
    private final FloorpicRepository floorpicRepository;
    private final NotificationRepository notificationRepository;

    @Override
    public ResponseEntity<? super GetBuildingResponseDto> getBuilding(Integer buildingId) {
        BuildingEntity buildingEntity = buildingRepository.findByBuildingId(buildingId);
        if (buildingEntity == null) throw new BusinessException(ErrorCode.BUILDING_NOT_FOUND);
        return GetBuildingResponseDto.success(buildingEntity);
    }

    @Override
    public ResponseEntity<? super GetBuildingListResponseDto> getBuildingList() {
        List<BuildingEntity> buildingEntities = buildingRepository.getBuildingList();
        return GetBuildingListResponseDto.success(buildingEntities);
    }

    @Override
    public ResponseEntity<? super CreateMarkerResponseDto> createMarker(String email, CreateMarkerRequestDto dto) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        if (!postRepository.existsById(dto.getPostId())) throw new BusinessException(ErrorCode.POST_NOT_FOUND);

        markerRepository.save(new MarkerEntity(dto, userEntity.getUserId()));
        return CreateMarkerResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetMarkerResponseDto> getMarker(Integer markerId) {
        MarkerEntity markerEntity = markerRepository.findByMarkerId(markerId);
        if (markerEntity == null) throw new BusinessException(ErrorCode.MARKER_NOT_FOUND);

        PostEntity postEntity = postRepository.findByPostId(markerEntity.getPostId());
        if (postEntity == null) throw new BusinessException(ErrorCode.POST_NOT_FOUND);

        return GetMarkerResponseDto.success(markerEntity);
    }

    @Override
    public ResponseEntity<? super PatchMarekrResponseDto> patchMarker(PatchMarkerRequestDto dto, String email, Integer markerId) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        MarkerEntity markerEntity = markerRepository.findByMarkerId(markerId);
        if (markerEntity == null) throw new BusinessException(ErrorCode.MARKER_NOT_FOUND);
        if (!markerEntity.getUserId().equals(userEntity.getUserId())) throw new BusinessException(ErrorCode.NO_PERMISSION);
        if (!postRepository.existsById(dto.getPostId())) throw new BusinessException(ErrorCode.POST_NOT_FOUND);

        markerEntity.patchMarker(dto);
        markerRepository.save(markerEntity);
        return PatchMarekrResponseDto.success();
    }

    @Override
    public ResponseEntity<? super DeleteMarkerResponseDto> deleteMarker(Integer markerId, String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        MarkerEntity markerEntity = markerRepository.findById(markerId).orElse(null);
        if (markerEntity == null) throw new BusinessException(ErrorCode.MARKER_NOT_FOUND);
        if (!markerEntity.getUserId().equals(userEntity.getUserId())) throw new BusinessException(ErrorCode.NO_PERMISSION);

        notificationRepository.deleteByMarkerId(markerEntity.getMarkerId());
        markerRepository.delete(markerEntity);
        return DeleteMarkerResponseDto.success();
    }

    @Override
    public ResponseEntity<? super GetTopMarkerResponseDto> getTopMarker() {
        LocalDateTime beforeWeek = LocalDateTime.now().minusWeeks(1);
        List<MarkerEntity> list = markerRepository.findMarkersByLikesSinceDate(beforeWeek);
        return GetTopMarkerResponseDto.success(list);
    }

    @Override
    public ResponseEntity<? super GetUserMarkerResponseDto> getUserMarker(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        List<MarkerEntity> list = markerRepository.findByUserId(userEntity.getUserId());
        return GetUserMarkerResponseDto.success(list);
    }

    @Override
    public ResponseEntity<? super GetSearchBuildingResponseDto> getSearchBuildingId(String word) {
        List<BuildingEntity> buildings = buildingRepository.findByBuildingCodeContains(word);
        if (buildings.isEmpty()) {
            buildings = buildingRepository.findByNameContains(word);
        }
        if (buildings.isEmpty()) throw new BusinessException(ErrorCode.BUILDING_NOT_FOUND);
        return GetSearchBuildingResponseDto.success(buildings.get(0).getBuildingId());
    }

    @Override
    public ResponseEntity<? super GetBuildingImageResponseDto> getBuildingImage(Integer buildingId) {
        List<FloorpicEntity> srcList = floorpicRepository.findByBuildingId(buildingId);
        return GetBuildingImageResponseDto.success(srcList);
    }

    @Transactional
    public void deleteMarkersAndNotificationsByPostId(Integer postId) {
        List<MarkerEntity> markers = markerRepository.findByPostId(postId);
        for (MarkerEntity marker : markers) {
            notificationRepository.deleteByMarkerId(marker.getMarkerId());
            markerRepository.delete(marker);
        }
    }
}
