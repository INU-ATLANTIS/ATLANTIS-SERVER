package com.atl.map.dto.request.post;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostChildCommentRequestDto {
    
    @NotBlank
    private String content;

}
