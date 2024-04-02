package com.atl.map.dto.response.marker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.common.ResponseCode;
import com.atl.map.common.ResponseMessage;
import com.atl.map.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class GetMarkerResponseDto extends ResponseDto{

    private final Integer postId;
    
    private GetMarkerResponseDto(Integer postId){
        super();
        this.postId = postId;
    }

    public static ResponseEntity<GetMarkerResponseDto> success(Integer postId){
        GetMarkerResponseDto result = new GetMarkerResponseDto(postId);
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
