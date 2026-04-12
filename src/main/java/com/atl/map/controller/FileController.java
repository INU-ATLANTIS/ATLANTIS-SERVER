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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class FileController {
    
    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<? super UploadFileResponseDto> upload(
        @RequestParam("file") MultipartFile file
    ){
        ResponseEntity<? super UploadFileResponseDto> response = fileService.upload(file);
        return response;
    }

    @GetMapping(value="{fileName}", produces={MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<Resource> getImage(
        @PathVariable("fileName") String fileName
    ){
        ResponseEntity<Resource> response = fileService.getImage(fileName);
        return response;
    }


}
