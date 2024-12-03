package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Violations;
import com.mycompany.myapp.repository.ViolationsRepository;
import com.mycompany.myapp.service.FileService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.jhipster.web.util.HeaderUtil;

@RestController
@RequestMapping("/api/files")
public class DetectResource {

    private final FileService fileService;

    private static final String ENTITY_NAME = "violations";
    private final ViolationsRepository violationsRepository;

    @Autowired
    public DetectResource(FileService fileService, ViolationsRepository violationsRepository) {
        this.fileService = fileService;
        this.violationsRepository = violationsRepository;
    }

    @PostMapping("")
    public ResponseEntity<Violations> createViolations(@RequestBody Violations violations) throws URISyntaxException {
        if (violations.getId() != null) {
            throw new BadRequestAlertException("A new violations cannot already have an ID", ENTITY_NAME, "idexists");
        }
        violations = violationsRepository.save(violations);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = fileService.uploadFile(file);
            return ResponseEntity.ok("File uploaded successfully: " + fileUrl);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error uploading file: " + e.getMessage());
        }
    }

    @GetMapping(value = "/{fileName}", produces = "video/mp4")
    public ResponseEntity<InputStreamResource> streamVideo(@PathVariable String fileName) {
        InputStream videoStream = fileService.getVideoStream(fileName);

        return ResponseEntity.ok().contentType(MediaType.parseMediaType("video/mp4")).body(new InputStreamResource(videoStream));
    }
}
