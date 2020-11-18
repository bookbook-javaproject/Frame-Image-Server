package com.frame.file.service.file;

import com.frame.file.dto.FileIdResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    ResponseEntity<Resource> loadFile(String id);
    FileIdResponse uploadFile(FileType fileType, MultipartFile file);
    FileIdResponse updateFile(String id, MultipartFile changedFile);
    void deleteFile(String id);
}