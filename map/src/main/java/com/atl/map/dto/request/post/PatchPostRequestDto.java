package com.atl.map.dto.request.post;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PatchPostRequestDto {
    
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private List<String> imageList;
}
