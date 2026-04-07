package com.atl.map.dto.response.marker;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;

import com.atl.map.entity.FloorpicEntity;

import lombok.Getter;

@Getter
public class GetBuildingImageResponseDto extends ResponseDto{
    
    List<FloorpicEntity> srcList;

    private GetBuildingImageResponseDto(List<FloorpicEntity> srcList){
        super(ResponseCode.SUCCESS, ResponseMessage.SUCCESS);
        this.srcList = srcList;
    }

    public static ResponseEntity<GetBuildingImageResponseDto> success(List<FloorpicEntity> srcList){
        GetBuildingImageResponseDto result = new GetBuildingImageResponseDto(srcList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
