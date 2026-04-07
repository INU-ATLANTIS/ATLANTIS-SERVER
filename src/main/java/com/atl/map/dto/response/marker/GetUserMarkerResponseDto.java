package com.atl.map.dto.response.marker;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.entity.MarkerEntity;

import lombok.Getter;

@Getter
public class GetUserMarkerResponseDto extends ResponseDto{
    
    List<MarkerEntity> userMarkerList;

    public GetUserMarkerResponseDto(List<MarkerEntity> userMarkerList){
        this.userMarkerList = userMarkerList;
    }

    public static ResponseEntity<GetUserMarkerResponseDto> success(List<MarkerEntity> userMarkerList) {
        GetUserMarkerResponseDto result = new GetUserMarkerResponseDto(userMarkerList);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistUser(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_USER, ResponseMessage.NOT_EXISTED_USER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
