package com.atl.map.entity;

import java.math.BigDecimal;

import com.atl.map.dto.request.marker.CreateMarkerRequestDto;
import com.atl.map.dto.request.marker.PatchMarkerRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity 
@Table(name = "marker") 
public class MarkerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Integer markerId;

    private BigDecimal x;

    private BigDecimal y;

    private String name;

    private Integer userId;

    private Integer postId;

    public MarkerEntity(CreateMarkerRequestDto dto, int userId){
        this.x = dto.getX();
        this.y = dto.getY();
        this.postId = dto.getPostId();
        this.name = dto.getName();
        this.userId = userId;
    }

    public void patchMarker(PatchMarkerRequestDto dto){
        this.x = dto.getX();
        this.y = dto.getY();
        this.postId = dto.getPostId();
        this.name = dto.getName();
    }

}
