package com.atl.map.dto.response.marker;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.entity.BuildingEntity;

import lombok.Getter;

@Getter
public class GetBuildingListResponseDto extends ResponseDto{
    
    List<BuildingEntity> buildingList = new ArrayList<>();

     private GetBuildingListResponseDto(List<BuildingEntity> resultSets){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.buildingList = BuildingEntity.copyList(resultSets);
    }

    public static ResponseEntity<GetBuildingListResponseDto> success(List<BuildingEntity> resultSets){
        GetBuildingListResponseDto result = new GetBuildingListResponseDto(resultSets);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}