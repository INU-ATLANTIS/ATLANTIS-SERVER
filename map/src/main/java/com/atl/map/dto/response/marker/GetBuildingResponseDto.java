package com.atl.map.dto.response.marker;

import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.ResponseDto;
import com.atl.map.entity.BuildingEntity;
import lombok.Getter;

@Getter
public class GetBuildingResponseDto extends ResponseDto {
    
    private Integer buildingId;
    private String name;
    private BigDecimal x;
    private BigDecimal y;
    private String office;
    private String phone;
    private String url;
    private String departments;
    private String buildingCode;

    private GetBuildingResponseDto(BuildingEntity buildingEntity){
        this.buildingId = buildingEntity.getBuildingId();
        this.name = buildingEntity.getName();
        this.x = buildingEntity.getX();
        this.y = buildingEntity.getY();
        this.office = buildingEntity.getOffice();
        this.phone = buildingEntity.getPhone();
        this.url = buildingEntity.getUrl();
        this.departments = buildingEntity.getDepartments();
        this.buildingCode = buildingEntity.getBuildingCode();
    }

    public static ResponseEntity<GetBuildingResponseDto> success(BuildingEntity buildingEntity){
        GetBuildingResponseDto responseBody = new GetBuildingResponseDto(buildingEntity);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }

}
