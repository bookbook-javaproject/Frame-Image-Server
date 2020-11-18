package com.frame.file.controller;

import com.frame.file.dto.FileIdResponse;
import com.frame.file.service.file.FileService;
import com.frame.file.service.file.FileType;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class FileController {

    private final FileService fileService;

    @GetMapping({"/file"})
    public ResponseEntity<Resource> loadFile(@RequestParam String id) {
        return fileService.loadFile(id);
    }

    @PostMapping("/file")
    @ResponseStatus(HttpStatus.CREATED)
    public FileIdResponse uploadFile(@RequestParam MultipartFile file) {
        return fileService.uploadFile(FileType.FILE, file);
    }

    @PostMapping("/image")
    @ResponseStatus(HttpStatus.CREATED)
    public FileIdResponse uploadImage(@RequestParam MultipartFile file) {
        return fileService.uploadFile(FileType.IMAGE, file);
    }
    @PutMapping("/file")
    public FileIdResponse updateFile(@RequestParam String id, @RequestParam MultipartFile changedFile) {
        return fileService.updateFile(id, changedFile);
    }

    @DeleteMapping({"/file", "/image"})
    public void deleteFile(@RequestParam String id) {
        fileService.deleteFile(id);
    }

}