package com.atl.map.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "floorpic")
public class FloorpicEntity {
    
    @Id
    private Integer floorid;

    private Integer floor;
    
    private String src;

    private Integer buildingId;
}
