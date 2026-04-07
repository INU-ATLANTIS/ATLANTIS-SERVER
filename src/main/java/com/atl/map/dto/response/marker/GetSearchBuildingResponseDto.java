package com.atl.map.dto.response.marker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class GetSearchBuildingResponseDto extends ResponseDto {
    
    private Integer buildingId;
    private GetSearchBuildingResponseDto(Integer buildingId){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.buildingId = buildingId;
    }

    public static ResponseEntity<GetSearchBuildingResponseDto> success(Integer buildingId){
        GetSearchBuildingResponseDto result = new GetSearchBuildingResponseDto(buildingId);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
