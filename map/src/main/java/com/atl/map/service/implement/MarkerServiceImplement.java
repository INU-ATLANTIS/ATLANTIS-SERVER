package com.atl.map.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.atl.map.dto.request.marker.CreateMarkerRequestDto;
import com.atl.map.dto.request.marker.PatchMarkerRequestDto;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.dto.response.marker.CreateMarkerResponseDto;
import com.atl.map.dto.response.marker.DeleteMarkerResponseDto;
import com.atl.map.dto.response.marker.GetBuildingListResponseDto;
import com.atl.map.dto.response.marker.GetBuildingResponseDto;
import com.atl.map.dto.response.marker.GetMarkerResponseDto;
import com.atl.map.dto.response.marker.PatchMarekrResponseDto;
import com.atl.map.entity.BuildingEntity;
import com.atl.map.entity.MarkerEntity;
import com.atl.map.entity.PostEntity;
import com.atl.map.entity.UserEntity;
import com.atl.map.repository.BuildingRepository;
import com.atl.map.repository.MarkerRepository;
import com.atl.map.repository.PostRepository;
import com.atl.map.repository.UserRepository;
import com.atl.map.service.MarkerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MarkerServiceImplement implements MarkerService{

    private final UserRepository userRepository;
    private final MarkerRepository markerRepository;
    private final PostRepository postRepository;
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
    @Override
    public ResponseEntity<? super GetBuildingListResponseDto> getBuildingList() {
        
        List<BuildingEntity> buildingEntities = new ArrayList<>();

        try{

            buildingEntities = buildingRepository.getBuildingList();

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return GetBuildingListResponseDto.success(buildingEntities);
    }
    @Override
    public ResponseEntity<? super CreateMarkerResponseDto> createMarker(String email,
            CreateMarkerRequestDto dto) {
              
        try{

            UserEntity userEntity = userRepository.findByEmail(email);
            if(userEntity == null) return CreateMarkerResponseDto.notExistUser();
            boolean existedPost = postRepository.existsById(dto.getPostId());
            if(!existedPost) return CreateMarkerResponseDto.notExistPost();
            MarkerEntity markerEntity = new MarkerEntity(dto, userEntity.getUserId());
            markerRepository.save(markerEntity);

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return CreateMarkerResponseDto.success();
    }
    
    @Override
    public ResponseEntity<? super GetMarkerResponseDto> getMarker(Integer markerId) 
    {
        PostEntity postEntity = null;
        try{
            MarkerEntity markerEntity = markerRepository.findByMarkerId(markerId);
            if (markerEntity == null) return GetMarkerResponseDto.notExistMarker();
            postEntity = postRepository.findByPostId(markerEntity.getPostId());
            if (postEntity == null) return GetMarkerResponseDto.notExistPost();
     
        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
   
        return GetMarkerResponseDto.success(postEntity.getPostId());
    }
    @Override
    public ResponseEntity<? super PatchMarekrResponseDto> patchMarker(PatchMarkerRequestDto dto, String email, Integer markerId) {
    
        try{
            UserEntity userEntity = userRepository.findByEmail(email);
            if(userEntity == null) return PatchMarekrResponseDto.notExistUser();
            MarkerEntity markerEntity = markerRepository.findByMarkerId(markerId);
            if(markerEntity == null) return PatchMarekrResponseDto.notExistMarker();
            if (!markerEntity.getUserId().equals(userEntity.getUserId())) {
                return DeleteMarkerResponseDto.noPermission();}
            boolean existedPost = postRepository.existsById(dto.getPostId());
            if(!existedPost) return PatchMarekrResponseDto.notExistPost();

            markerEntity.patchMarker(dto);
            markerRepository.save(markerEntity);

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }

        return PatchMarekrResponseDto.success();
    }
    @Override
    public ResponseEntity<? super DeleteMarkerResponseDto> deleteMarker(Integer markerId, String email) {
    
        try{
            UserEntity userEntity = userRepository.findByEmail(email);
            if (userEntity == null) {
                return DeleteMarkerResponseDto.notExistUser();}
            MarkerEntity markerEntity = markerRepository.findById(markerId).orElse(null);
            if (markerEntity == null) {
                return DeleteMarkerResponseDto.notExistMarker();}
            if (!markerEntity.getUserId().equals(userEntity.getUserId())) {
                return DeleteMarkerResponseDto.noPermission();}
            markerRepository.delete(markerEntity);

        }catch(Exception exception){
            exception.printStackTrace();
            return ResponseDto.databaseError();
        }
        return DeleteMarkerResponseDto.success();
    }
   
}
