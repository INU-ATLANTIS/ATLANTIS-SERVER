package com.atl.map.dto.response.marker;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;
import com.atl.map.entity.MarkerEntity;

import lombok.Getter;

@Getter
public class GetMarkerResponseDto extends ResponseDto{

    private final Integer postId;
    private final BigDecimal x;
    private final BigDecimal y;
    private final String type;
    
    private GetMarkerResponseDto(MarkerEntity markerEntity){
        super();
        this.postId = markerEntity.getPostId();
        this.x = markerEntity.getX();
        this.y = markerEntity.getY();
        this.type = markerEntity.getType();
    }

    public static ResponseEntity<GetMarkerResponseDto> success(MarkerEntity markerEntity){
        GetMarkerResponseDto result = new GetMarkerResponseDto(markerEntity);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public static ResponseEntity<ResponseDto> notExistPost(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_POST, ResponseMessage.NOT_EXISTED_POST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }

    public static ResponseEntity<ResponseDto> notExistMarker(){
        ResponseDto responseBody = new ResponseDto(ResponseCode.NOT_EXISTED_MARKER, ResponseMessage.NOT_EXISTED_MARKER);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseBody);
    }
}
