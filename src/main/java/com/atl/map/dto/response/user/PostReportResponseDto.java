package com.atl.map.dto.response.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.ResponseDto;

public class PostReportResponseDto extends ResponseDto {

    private PostReportResponseDto() {
        super();
    }

    public static ResponseEntity<PostReportResponseDto> success() {
        PostReportResponseDto responseBody = new PostReportResponseDto();
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
