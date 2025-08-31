package com.example.absoluteweb.server.controllers;

import com.example.absoluteweb.server.services.PatchService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/files")
public class PatchController {

    private final PatchService patchService;

    public PatchController(PatchService patchService) {
        this.patchService = patchService;
    }

    @GetMapping("/**")
    public ResponseEntity<?> getFile(HttpServletRequest request) {
        try{
            // Отримуємо повний шлях після "/files/"
            String fullPath = request.getRequestURI().replace("/api/files/", "");
            Resource fileResource = patchService.loadAsResource(fullPath);
            String contentType = "application/octet-stream";
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                    .body(fileResource);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Patch exception : " + e.getMessage());
        }
    }
}
