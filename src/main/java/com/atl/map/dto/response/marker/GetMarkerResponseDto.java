package com.atl.map.dto.response.marker;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.ResponseDto;
import com.atl.map.entity.MarkerEntity;

import lombok.Getter;

@Getter
public class GetMarkerResponseDto extends ResponseDto {

    private final Integer postId;
    private final BigDecimal x;
    private final BigDecimal y;
    private final String type;

    private GetMarkerResponseDto(MarkerEntity markerEntity) {
        super();
        this.postId = markerEntity.getPostId();
        this.x = markerEntity.getX();
        this.y = markerEntity.getY();
        this.type = markerEntity.getType();
    }

    public static ResponseEntity<GetMarkerResponseDto> success(MarkerEntity markerEntity) {
        GetMarkerResponseDto result = new GetMarkerResponseDto(markerEntity);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
