package com.files.api.controller;

import com.files.api.entity.File;
import com.files.api.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class FileController {
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload-file")
    public ResponseEntity<Boolean> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            fileService.uploadFile(file.getInputStream(), StringUtils.cleanPath(file.getOriginalFilename()));
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/upload-file-database")
    public ResponseEntity<Boolean> uploadFileInDataBase(@RequestParam("file") MultipartFile file) {
        try {
            fileService.uploadFileInDB(file);
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/upload-files")
    public ResponseEntity<Boolean> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
        try {
            for (MultipartFile file : files) {
                fileService.uploadFile(file.getInputStream(), StringUtils.cleanPath(file.getOriginalFilename()));
            }
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/files")
    public ResponseEntity<List<String>> getFiles() {
        try {
            return ResponseEntity.ok(fileService.getFiles());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/file-by-id")
    public ResponseEntity<File> getFileById(@RequestBody String id) {
        try {
            return ResponseEntity.ok(fileService.getFile(Long.parseLong(id)));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/files-from-database")
    public ResponseEntity<List<File>> getFilesFromDataBase() {
        try {
            return ResponseEntity.ok(fileService.getAllFilesDB());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}
