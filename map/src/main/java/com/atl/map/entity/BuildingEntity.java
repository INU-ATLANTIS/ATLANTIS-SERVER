package com.atl.map.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "building")
public class BuildingEntity {

    @Id
    @NotNull
    private Integer buildingId;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal x;

    @NotNull
    private BigDecimal y;

    @NotNull
    private String office;

    @NotNull
    private String phone;

    @NotNull
    private String url;

    @NotNull
    private String departments;

    @Column(name="code")
    private String buildingCode;

    public BuildingEntity(BuildingEntity buildingEntity){
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

    public static List<BuildingEntity> copyList(List<BuildingEntity> resultSets) {
        List<BuildingEntity> list = new ArrayList<>();
        for(BuildingEntity resultSet : resultSets){
            BuildingEntity buildingEntity = new BuildingEntity(resultSet);
            list.add(buildingEntity);
        }
        return list;
    }
    
}
