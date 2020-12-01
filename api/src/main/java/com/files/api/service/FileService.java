package com.files.api.service;

import com.files.api.constant.FileDirectoryConstants;
import com.files.api.entity.File;
import com.files.api.repository.FileRepository;
import com.files.api.service.exception.FileNotFoundException;
import com.files.api.service.exception.FileStorageException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileService implements IFile {

    private final FileDirectoryConstants fileDirectoryConstants;
    private final Path fileStorageLocation;
    private final FileRepository fileRepository;

    public FileService(FileDirectoryConstants fileDirectoryConstants, FileRepository fileRepository) {
        this.fileDirectoryConstants = fileDirectoryConstants;
        this.fileRepository = fileRepository;
        this.fileStorageLocation = Paths.get(fileDirectoryConstants.getUploadDir())
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.");
        }
    }

    @Override
    public void uploadFile(InputStream file, String fileName) {
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file, targetLocation, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!");
        }
    }

    @Override
    public List<String> getFiles() throws IOException {

        return Files.walk(Paths.get(fileDirectoryConstants.getUploadDir()))
                .filter(Files::isRegularFile)
                .map(file -> file.getFileName().toString())
                .collect(Collectors.toList());
    }

    public File uploadFileInDB(MultipartFile file) throws IOException {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        return fileRepository.save(File.builder().name(fileName).type(file.getContentType()).data(file.getBytes()).build());
    }

    public File getFile(Long id) {
        return fileRepository.findById(id).orElseThrow(() -> new FileNotFoundException("File not found"));
    }

    public Stream<File> getAllFiles() {
        return fileRepository.findAll().stream();
    }

}
