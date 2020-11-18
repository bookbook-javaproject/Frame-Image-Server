package com.frame.file.service.file;

import com.frame.file.dto.FileIdResponse;
import com.frame.file.entity.file.FileEntity;
import com.frame.file.entity.file.FileRepository;
import com.frame.file.error.exception.FileExtensionNotAllowedException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    @Value("${file.upload.directory}")
    private String uploadDirectory;

    @Value("${file.upload.ban-list}")
    private List<String> banedExtension;

    @SneakyThrows
    @Override
    public ResponseEntity<Resource> loadFile(String id) {
        FileEntity fileEntity = fileRepository.findById(id)
                .orElseThrow(FileNotFoundException::new);

        Resource resource = loadFileAsResource(fileEntity.getFilePath());
        if (fileEntity.getFileType().equals(FileType.FILE)) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, String.format(
                            "attachment; filename=\"%s.%s\"",fileEntity.getFileName(), fileEntity.getFileExtension()
                    ))
                    .body(resource);
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("image/"+fileEntity.getFileExtension()))
                    .body(resource);
        }
    }

    @SneakyThrows
    @Override
    public FileIdResponse uploadFile(FileType fileType, MultipartFile file) {
        String fileId = UUID.randomUUID().toString();
        String[] fileFullName = file.getOriginalFilename().split("\\.");
        String fileName = fileFullName[0];
        String fileExtension = fileFullName[1];

        String path = Paths.get(uploadDirectory, fileId).toString();
        if (banedExtension.contains(fileExtension))
            throw new FileExtensionNotAllowedException();

        FileEntity fileEntity = fileRepository.save(
                FileEntity.builder()
                        .id(fileId)
                        .fileName(fileName)
                        .fileExtension(fileExtension.toLowerCase())
                        .fileType(fileType)
                        .filePath(path)
                        .build()
        );

        file.transferTo(new File(fileEntity.getFilePath()));
        return FileIdResponse.builder().fileId(fileId).build();
    }

    @SneakyThrows
    @Override
    public FileIdResponse updateFile(String id, MultipartFile changedFile) {
        FileEntity fileEntity = fileRepository.findById(id)
                .orElseThrow(FileNotFoundException::new);

        String[] fileFullName = changedFile.getOriginalFilename().split("\\.");
        String fileName = fileFullName[0];
        String fileExtension = fileFullName[1];

        if (banedExtension.contains(fileExtension))
            throw new FileExtensionNotAllowedException();

        changedFile.transferTo(new File(fileEntity.getFilePath()));
        fileRepository.save(fileEntity.updateFile(fileName, fileExtension.toLowerCase()));

        return FileIdResponse.builder().fileId(id).build();
    }

    @SneakyThrows
    @Override
    public void deleteFile(String id) {
        FileEntity fileEntity = fileRepository.findById(id)
                .orElseThrow(FileNotFoundException::new);

        Files.deleteIfExists(Paths.get(fileEntity.getFilePath()));
        fileRepository.save(fileEntity.deleteFile());
    }

    private Resource loadFileAsResource(String fileName) throws FileNotFoundException {
        try {
            Path filePath = new File(fileName).toPath();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileNotFoundException();
            }
        } catch (Exception ex) {
            throw new FileNotFoundException();
        }
    }

}