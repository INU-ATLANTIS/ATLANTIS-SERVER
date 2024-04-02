package com.atl.map.dto.request.marker;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateMarkerRequestDto {
    
    @NotNull
    private String name;
    @NotNull
    private BigDecimal x;
    @NotNull
    private BigDecimal y;
    @NotNull
    private Integer postId;

}
