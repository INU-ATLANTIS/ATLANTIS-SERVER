package com.atl.map.dto.response.marker;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.ResponseDto;
import com.atl.map.entity.MarkerEntity;

import lombok.Getter;

@Getter
public class GetTopMarkerResponseDto extends ResponseDto{
    
    private List<MarkerEntity> topList;

    public GetTopMarkerResponseDto(List<MarkerEntity> list){
        super();
        this.topList = list;
    }

    public static ResponseEntity<GetTopMarkerResponseDto> success(List<MarkerEntity> topList){
        GetTopMarkerResponseDto result = new GetTopMarkerResponseDto(topList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
