package com.atl.map.service.implement;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.atl.map.dto.response.file.UploadFileResponseDto;
import com.atl.map.exception.BusinessException;
import com.atl.map.exception.ErrorCode;
import com.atl.map.service.FileService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FileServiceImplement implements FileService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png"
    );

    @Value("${file.path}")
    private String filePath;
    @Value("${file.url}")
    private String fileUrl;

    @Override
    public ResponseEntity<? super UploadFileResponseDto> upload(MultipartFile file) {
        
        if (file == null || file.isEmpty()) throw new BusinessException(ErrorCode.INVALID_FILE);

        String originalFileName = file.getOriginalFilename();
        if (!StringUtils.hasText(originalFileName)) {
            log.warn("파일 업로드 실패 - 파일명이 비어 있음");
            throw new BusinessException(ErrorCode.INVALID_FILE);
        }
        if (!isAllowedContentType(file.getContentType())) {
            log.warn("파일 업로드 실패 - 허용되지 않은 contentType: {}", file.getContentType());
            throw new BusinessException(ErrorCode.INVALID_FILE);
        }

        String extension = extractExtension(originalFileName);
        if (extension == null) {
            log.warn("파일 업로드 실패 - 확장자 추출 불가: {}", originalFileName);
            throw new BusinessException(ErrorCode.INVALID_FILE);
        }

        String uuid = UUID.randomUUID().toString();
        String saveFileName = uuid + extension;
        Path basePath = Path.of(filePath).toAbsolutePath().normalize();
        Path savePath = basePath.resolve(saveFileName).normalize();

        if (!savePath.startsWith(basePath)) {
            log.warn("파일 업로드 실패 - 비정상 저장 경로: {}", savePath);
            throw new BusinessException(ErrorCode.INVALID_FILE);
        }

        try{
            Files.createDirectories(basePath);
            file.transferTo(new File(savePath.toString()));
        }catch(Exception exception){
            log.error("파일 업로드 실패 - filename: {}", originalFileName, exception);
            throw new BusinessException(ErrorCode.FILE_UPLOAD_FAIL);
        }

        String url = fileUrl + saveFileName;
        return UploadFileResponseDto.success(url);
    }

    @Override
    public ResponseEntity<Resource> getImage(String filename) {
        
        if (!StringUtils.hasText(filename)) {
            throw new BusinessException(ErrorCode.INVALID_FILE);
        }

        try{
            Path basePath = Path.of(filePath).toAbsolutePath().normalize();
            Path requestedPath = basePath.resolve(filename).normalize();
            String normalizedFileName = Path.of(filename).getFileName().toString();

            if (!normalizedFileName.equals(filename) || !requestedPath.startsWith(basePath)) {
                log.warn("파일 조회 실패 - 잘못된 파일명 요청: {}", filename);
                throw new BusinessException(ErrorCode.INVALID_FILE);
            }

            if (!Files.exists(requestedPath) || !Files.isReadable(requestedPath)) {
                log.warn("파일 조회 실패 - 파일이 없거나 읽을 수 없음: {}", filename);
                throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
            }

            Resource resource = new UrlResource(requestedPath.toUri());
            return ResponseEntity.ok(resource);
            
        } catch (BusinessException exception) {
            throw exception;
        }catch(Exception exception){
            log.error("파일 조회 실패 - filename: {}", filename, exception);
            throw new BusinessException(ErrorCode.FILE_NOT_FOUND);
        }
    }

    private boolean isAllowedContentType(String contentType) {
        return contentType != null && ALLOWED_CONTENT_TYPES.contains(contentType);
    }

    private String extractExtension(String originalFileName) {
        int extensionIndex = originalFileName.lastIndexOf(".");
        if (extensionIndex <= 0 || extensionIndex == originalFileName.length() - 1) {
            return null;
        }
        return originalFileName.substring(extensionIndex);
    }
    
}
