package com.cms.backend.controller;

import com.cms.backend.pojo.Attachment;
import com.cms.backend.service.AttachmentService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {
    public final AttachmentService attachmentService;

    private final Logger logger = LoggerFactory.getLogger(AttachmentController.class);

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @DeleteMapping(value = "/delete")
    public ResponseEntity<Void> deleteAttachment(@RequestBody Attachment attachment) {
        attachmentService.removeById(attachment);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/get")
    public ResponseEntity<AttachmentDTO> getAttachment(@RequestParam int id) {
        var attachment = attachmentService.getById(id);
        return ResponseEntity.ok(new AttachmentDTO(attachment.getName(), attachment.getUrl()));
    }

    @PostMapping(value = "upload")
    public ResponseEntity<UploadedAttachment> uploadAttachment(MultipartFile file, String name) {
        var f = new File("D:\\nginx\\nginx-1.26.2\\ICPRFiles\\" + name);

        try {
            if (!f.exists()) {
                if (!f.createNewFile()) {
                    throw new IOException();
                }
            }
            FileUtils.writeByteArrayToFile(f, file.getBytes());
        } catch (IOException e) {
            logger.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        var url = "http://localhost:65/" + name;
        var attachment = new Attachment(
                url,
                name
        );
        attachmentService.save(attachment);
        return ResponseEntity.ok(new UploadedAttachment(
                attachment.getId(), url
        ));
    }

    @Data
    @AllArgsConstructor
    public static class AttachmentDTO {
        private String name;
        private String url;
    }

    @Data
    @AllArgsConstructor
    public static class UploadedAttachment {
        private int id;
        private String url;
    }
}
