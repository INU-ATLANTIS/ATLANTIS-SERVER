package com.atl.map.dto.response.file;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.atl.map.dto.response.ResponseDto;

import lombok.Getter;

@Getter
public class UploadFileResponseDto extends ResponseDto {

    private final String fileUrl;

    private UploadFileResponseDto(String fileUrl) {
        super();
        this.fileUrl = fileUrl;
    }

    public static ResponseEntity<UploadFileResponseDto> success(String fileUrl) {
        UploadFileResponseDto responseBody = new UploadFileResponseDto(fileUrl);
        return ResponseEntity.status(HttpStatus.OK).body(responseBody);
    }
}
