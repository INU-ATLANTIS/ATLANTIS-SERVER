package com.atl.map.dto.request.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List; 

@Getter
@Setter
@NoArgsConstructor
public class CreatePostRequestDto {

    @NotBlank
    private String title;
    @NotBlank
    private String content;
    private List<String> imageList; 
    private int buildingId;
}
