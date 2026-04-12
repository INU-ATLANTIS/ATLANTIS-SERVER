package com.atl.map.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.atl.map.dto.response.file.UploadFileResponseDto;
import com.atl.map.service.FileService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
@Tag(name = "File", description = "파일 업로드 및 이미지 조회 API")
public class FileController {
    
    private final FileService fileService;

    @PostMapping("/upload")
    @Operation(summary = "파일 업로드", description = "이미지 파일을 업로드하고 접근 URL을 반환합니다.")
    public ResponseEntity<? super UploadFileResponseDto> upload(
        @RequestParam("file") MultipartFile file
    ){
        ResponseEntity<? super UploadFileResponseDto> response = fileService.upload(file);
        return response;
    }

    @GetMapping(value="{fileName}", produces={MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @Operation(summary = "이미지 조회", description = "파일명으로 업로드된 이미지를 조회합니다.")
    public ResponseEntity<Resource> getImage(
        @PathVariable("fileName") String fileName
    ){
        ResponseEntity<Resource> response = fileService.getImage(fileName);
        return response;
    }


}
