package com.atl.map.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

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
}
