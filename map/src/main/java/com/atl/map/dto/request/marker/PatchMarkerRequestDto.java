package com.atl.map.dto.request.marker;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatchMarkerRequestDto {
    
    private String name;
    private BigDecimal x;
    private BigDecimal y;
    private int postId;
    private String type;
}
