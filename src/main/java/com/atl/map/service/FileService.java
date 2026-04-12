package com.atl.map.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.atl.map.dto.response.file.UploadFileResponseDto;

public interface FileService {
    
    ResponseEntity<? super UploadFileResponseDto> upload(MultipartFile file);
    ResponseEntity<Resource> getImage(String filename);
}
