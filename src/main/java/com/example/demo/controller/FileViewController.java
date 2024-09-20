package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/files")
public class FileViewController {
    private final Path cvFileStorageLocation;
    private final Path logoFileStorageLocation;
    private final Path avatarFileStorageLocation;

    public FileViewController(@Value("${file.upload-dir-cv}") String uploadDirCv,
                              @Value("${file.upload-dir-logo}") String uploadDirLogo,
                              @Value("${file.upload-dir-avatar}") String uploadDirAvatar) {
        this.cvFileStorageLocation = Paths.get(uploadDirCv).toAbsolutePath().normalize();
        this.logoFileStorageLocation = Paths.get(uploadDirLogo).toAbsolutePath().normalize();
        this.avatarFileStorageLocation = Paths.get(uploadDirAvatar).toAbsolutePath().normalize();
    }

    @GetMapping("/cv/{filename:.+}")
    public ResponseEntity<Resource> viewFile(@PathVariable String filename) {
        return serveFile(cvFileStorageLocation, filename);
    }

    @GetMapping("/logo/{filename:.+}")
    public ResponseEntity<Resource> viewLogo(@PathVariable String filename) {
        return serveFile(logoFileStorageLocation, filename);
    }

    @GetMapping("/avatar/{filename:.+}")
    public ResponseEntity<Resource> viewAvatar(@PathVariable String filename) {
        return serveFile(avatarFileStorageLocation, filename);
    }

    // Phương thức này sẽ trả về file cho client
    private ResponseEntity<Resource> serveFile(Path fileStorageLocation, String filename) {
        try {
            // lấy đường dẫn file
            Path filePath = fileStorageLocation.resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            // kiểm tra xem file có tồn tại không
            if (resource.exists()) {
                String contentType = determineContentType(filename);
                // trả về file cho client
                return ResponseEntity.ok()
                        // set kiểu file (Content-Type: application/pdf, image/jpeg, ...) vào giao thức HTTP
                        .contentType(MediaType.parseMediaType(contentType))
                        // set header để client có thể download file
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                        // set nội dung trả về
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Phương thức này sẽ xác định kiểu của file
    private String determineContentType(String filename) {
        if (filename.toLowerCase().endsWith(".pdf")) {
            return "application/pdf";
        } else if (filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (filename.toLowerCase().endsWith(".png")) {
            return "image/png";
        } else if (filename.toLowerCase().endsWith(".gif")) {
            return "image/gif";
        } else if (filename.toLowerCase().endsWith(".txt")) {
            return "text/plain";
        } else if (filename.toLowerCase().endsWith(".html") || filename.toLowerCase().endsWith(".htm")) {
            return "text/html";
        } else if (filename.toLowerCase().endsWith(".mp4")) {
            return "video/mp4";
        } else if (filename.toLowerCase().endsWith(".mp3")) {
            return "audio/mpeg";
        }
        return "application/octet-stream"; // Default binary file type
    }
}