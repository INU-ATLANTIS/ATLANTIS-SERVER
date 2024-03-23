package com.atl.map.service.implement;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.atl.map.service.FileService;

@Service
public class FileServiceImplement implements FileService {

    @Value("${file.path}")
    private String filePath;
    @Value("${file.url}")
    private String fileUrl;

    @Override
    public String upload(MultipartFile file) {
        
        if(file.isEmpty()) return null;

        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String uuid = UUID.randomUUID().toString();
        String savaFileName = uuid + extension;
        String savaPath = filePath + savaFileName;

        try{
            file.transferTo(new File(savaPath));
        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }

        String url = fileUrl + savaFileName;
        return url;
    }

    @Override
    public Resource getImag(String filename) {
        
        Resource resource = null;

        try{
            resource = new UrlResource("file:" + filePath + filename);
            
        }catch(Exception exception){
            exception.printStackTrace();
            return null;
        }

        return resource;
    }
    
}
