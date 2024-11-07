package com.movieflix.controllers;

import com.movieflix.services.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file/")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${project.poster}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFileHandler(@RequestPart MultipartFile file) throws IOException {
        String uploadedFileName = fileService.uploadFile(path, file);
        return ResponseEntity.ok("File uploaded : " + uploadedFileName);
    }

    @GetMapping(value = "/{fileName}")
    public void getFileHandler(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        InputStream resourceFile = fileService.getResourceFile(path, fileName);
        if (resourceFile != null) {
            response.setContentType(MediaType.IMAGE_PNG_VALUE);
            StreamUtils.copy(resourceFile, response.getOutputStream());
            resourceFile.close(); // Good practice to close streams when done.
            response.getOutputStream().flush(); // Ensure all data is written out.
            response.getOutputStream().close(); // Close to free resources.
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
